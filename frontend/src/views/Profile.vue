<template>
  <div class="profile">
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card>
          <div class="user-info">
            <el-avatar :size="80" :src="userInfo.avatar">
              {{ userInfo.nickname?.charAt(0) || 'U' }}
            </el-avatar>
            <h3>{{ userInfo.nickname || '用户' }}</h3>
            <p>{{ userInfo.username }}</p>
            <el-tag :type="userInfo.role === 'admin' ? 'danger' : 'primary'" size="small">
              {{ userInfo.role === 'admin' ? '管理员' : '普通用户' }}
            </el-tag>
          </div>
        </el-card>
      </el-col>
      <el-col :span="16">
        <el-card>
          <template #header>
            <span>个人信息</span>
          </template>
          <el-form :model="form" label-width="100px">
            <el-form-item label="昵称">
              <el-input v-model="form.nickname" />
            </el-form-item>
            <el-form-item label="邮箱">
              <el-input v-model="form.email" />
            </el-form-item>
            <el-form-item label="手机号">
              <el-input v-model="form.phone" />
            </el-form-item>
            <el-form-item label="头像">
              <div style="display: flex; align-items: center; gap: 16px">
                <el-avatar :size="60" :src="avatarPreview">
                  {{ form.nickname?.charAt(0) || 'U' }}
                </el-avatar>
                <el-upload
                  action="/api/file/upload"
                  :show-file-list="false"
                  :on-success="handleAvatarSuccess"
                  :before-upload="beforeAvatarUpload"
                  accept="image/*"
                >
                  <el-button type="primary" size="small">上传头像</el-button>
                </el-upload>
              </div>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="updateProfile">保存修改</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="24">
        <el-card>
          <template #header>
            <span>学习概览</span>
          </template>
          <el-descriptions :column="3" border>
            <el-descriptions-item label="学习天数">{{ studyStats.days }}</el-descriptions-item>
            <el-descriptions-item label="完成章节">{{ studyStats.chapters }}</el-descriptions-item>
            <el-descriptions-item label="错题数量">{{ studyStats.wrongQuestions }}</el-descriptions-item>
            <el-descriptions-item label="总学习时长">{{ studyStats.totalTime }} 小时</el-descriptions-item>
            <el-descriptions-item label="本周学习">{{ studyStats.weekTime }} 小时</el-descriptions-item>
            <el-descriptions-item label="连续学习">{{ studyStats.streak }} 天</el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="24">
        <el-card>
          <template #header>
            <span>最近活动</span>
          </template>
          <el-timeline>
            <el-timeline-item
              v-for="(activity, index) in recentActivities"
              :key="index"
              :timestamp="activity.time"
              :type="activity.type"
            >
              {{ activity.content }}
            </el-timeline-item>
          </el-timeline>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { useUserStore } from '../stores/user'
import { userApi, progressApi, wrongQuestionApi } from '../api'
import { ElMessage } from 'element-plus'

const userStore = useUserStore()
const userInfo = ref(userStore.userInfo || {})
const form = reactive({
  nickname: userInfo.value.nickname || '',
  email: userInfo.value.email || '',
  phone: userInfo.value.phone || '',
  avatar: userInfo.value.avatar || ''
})

// 头像预览
const avatarPreview = computed(() => {
  return form.avatar || ''
})

// 头像上传前验证
const beforeAvatarUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt5M = file.size / 1024 / 1024 < 5

  if (!isImage) {
    ElMessage.error('只能上传图片文件!')
  }
  if (!isLt5M) {
    ElMessage.error('图片大小不能超过 5MB!')
  }
  return isImage && isLt5M
}

// 头像上传成功回调
const handleAvatarSuccess = (response) => {
  if (response.code === 200) {
    form.avatar = response.data.url
    ElMessage.success('头像上传成功')
  } else {
    ElMessage.error(response.message || '上传失败')
  }
}

const studyStats = reactive({
  days: 0,
  chapters: 0,
  wrongQuestions: 0,
  totalTime: 0,
  weekTime: 0,
  streak: 0
})

const recentActivities = ref([])

const updateProfile = async () => {
  try {
    await userApi.updateUser({
      id: userInfo.value.id,
      ...form
    })
    userStore.setUserInfo({ ...userInfo.value, ...form })
    ElMessage.success('个人信息已更新')
  } catch (e) {
    ElMessage.error('更新失败')
  }
}

const loadStats = async () => {
  try {
    const [progressRes, wrongRes] = await Promise.all([
      progressApi.getByUserId(userInfo.value.id),
      wrongQuestionApi.getByUserId(userInfo.value.id)
    ])

    const progressList = progressRes.data || []
    const wrongList = wrongRes.data || []

    studyStats.chapters = progressList.filter(p => p.status === 'completed').length
    studyStats.wrongQuestions = wrongList.length
    
    const totalMinutes = progressList.reduce((sum, p) => sum + (p.studyTime || 0), 0)
    studyStats.totalTime = Math.round(totalMinutes / 60)
    
    // 计算本周学习时长
    const now = new Date()
    const weekAgo = new Date(now.getTime() - 7 * 24 * 60 * 60 * 1000)
    const weekProgress = progressList.filter(p => {
      if (!p.lastStudyTime) return false
      return new Date(p.lastStudyTime) >= weekAgo
    })
    const weekMinutes = weekProgress.reduce((sum, p) => sum + (p.studyTime || 0), 0)
    studyStats.weekTime = Math.round(weekMinutes / 60)

    // 计算学习天数和连续天数
    const studyDays = new Set()
    progressList.forEach(p => {
      if (p.lastStudyTime) {
        studyDays.add(new Date(p.lastStudyTime).toDateString())
      }
    })
    studyStats.days = studyDays.size

    // 简单计算连续学习天数
    const today = new Date()
    let streak = 0
    for (let i = 0; i < 30; i++) {
      const checkDate = new Date(today.getTime() - i * 24 * 60 * 60 * 1000)
      if (studyDays.has(checkDate.toDateString())) {
        streak++
      } else if (i > 0) {
        break
      }
    }
    studyStats.streak = streak

    // 生成最近活动
    recentActivities.value = []
    progressList.slice(0, 5).forEach(p => {
      if (p.lastStudyTime) {
        recentActivities.value.push({
          content: `学习了章节 ${p.chapterId}`,
          time: new Date(p.lastStudyTime).toLocaleString('zh-CN'),
          type: p.status === 'completed' ? 'success' : 'primary'
        })
      }
    })

    wrongList.slice(0, 3).forEach(w => {
      recentActivities.value.push({
        content: `做错了一道题目`,
        time: new Date(w.lastWrongTime).toLocaleString('zh-CN'),
        type: 'warning'
      })
    })

    recentActivities.value.sort((a, b) => new Date(b.time) - new Date(a.time))
    recentActivities.value = recentActivities.value.slice(0, 8)
  } catch (e) {
    console.error(e)
  }
}

onMounted(() => {
  loadStats()
})
</script>

<style scoped>
.user-info {
  text-align: center;
  padding: 20px 0;
}

.user-info h3 {
  margin: 16px 0 8px;
}

.user-info p {
  color: #666;
  margin-bottom: 12px;
}
</style>
