<template>
  <div class="home">
    <el-row :gutter="20">
      <el-col :span="24">
        <div class="welcome-card">
          <h2>欢迎回来，{{ userStore.userInfo?.nickname || '同学' }}！</h2>
          <p>坚持每天学习，408考研加油！</p>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="6" v-for="subject in subjects" :key="subject.name">
        <el-card shadow="hover" class="subject-card">
          <template #header>
            <div class="card-header">
              <span>{{ subject.name }}</span>
              <el-tag :type="subject.tagType" size="small">{{ subject.chapters }}章</el-tag>
            </div>
          </template>
          <div class="progress-info">
            <p>学习进度</p>
            <el-progress :percentage="subject.progress" :stroke-width="10" />
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>今日学习任务</span>
          </template>
          <el-timeline>
            <el-timeline-item
              v-for="(task, index) in tasks"
              :key="index"
              :type="task.done ? 'success' : 'primary'"
              :timestamp="task.time"
            >
              {{ task.content }}
            </el-timeline-item>
          </el-timeline>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>快速入口</span>
          </template>
          <div class="quick-links">
            <el-button type="primary" @click="$router.push('/ai-chat')">
              <el-icon><ChatDotRound /></el-icon> AI答疑
            </el-button>
            <el-button type="success" @click="$router.push('/ai-practice')">
              <el-icon><EditPen /></el-icon> AI练习
            </el-button>
            <el-button type="warning" @click="$router.push('/wrong-questions')">
              <el-icon><WarningFilled /></el-icon> 错题本
            </el-button>
            <el-button type="info" @click="$router.push('/resources')">
              <el-icon><FolderOpened /></el-icon> 资源中心
            </el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { reactive, onMounted } from 'vue'
import { useUserStore } from '../stores/user'
import { chapterApi, progressApi } from '../api'

const userStore = useUserStore()

const subjects = reactive([
  { name: '数据结构', chapters: 6, progress: 0, tagType: 'primary' },
  { name: '计算机组成原理', chapters: 7, progress: 0, tagType: 'success' },
  { name: '操作系统', chapters: 5, progress: 0, tagType: 'warning' },
  { name: '计算机网络', chapters: 6, progress: 0, tagType: 'info' }
])

const tasks = reactive([
  { content: '复习数据结构 - 二叉树章节', time: '09:00', done: false },
  { content: '完成计算机组成原理练习题', time: '14:00', done: false },
  { content: 'AI答疑 - 操作系统疑问', time: '16:00', done: false },
  { content: '整理错题本', time: '20:00', done: false }
])

onMounted(async () => {
  try {
    const res = await chapterApi.getAll()
    const chapters = res.data || []
    const progressRes = await progressApi.getByUserId(userStore.userInfo.id)
    const progressList = progressRes.data || []

    subjects.forEach(subject => {
      const subjectChapters = chapters.filter(c => c.subject === subject.name)
      const completed = subjectChapters.filter(ch =>
        progressList.some(p => p.chapterId === ch.id && p.status === 'completed')
      )
      subject.progress = subjectChapters.length > 0
        ? Math.round((completed.length / subjectChapters.length) * 100)
        : 0
    })
  } catch (e) {
    console.error(e)
  }
})
</script>

<style scoped>
.welcome-card {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  padding: 30px;
  border-radius: 12px;
}

.welcome-card h2 {
  margin-bottom: 8px;
}

.subject-card .card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.progress-info p {
  margin-bottom: 8px;
  color: #666;
  font-size: 14px;
}

.quick-links {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}
</style>
