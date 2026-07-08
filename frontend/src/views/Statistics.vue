<template>
  <div class="statistics">
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card>
          <div class="stat-item">
            <div class="stat-value">{{ totalStudyTime }}</div>
            <div class="stat-label">总学习时长（小时）</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <div class="stat-item">
            <div class="stat-value">{{ completedChapters }}</div>
            <div class="stat-label">已完成章节</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <div class="stat-item">
            <div class="stat-value">{{ wrongCount }}</div>
            <div class="stat-label">错题数量</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>各科学习进度</span>
          </template>
          <div ref="progressChart" style="height: 300px"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>学习时间分布</span>
          </template>
          <div ref="timeChart" style="height: 300px"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="24">
        <el-card>
          <template #header>
            <span>学习详情</span>
          </template>
          <el-table :data="progressList" style="width: 100%">
            <el-table-column prop="chapterName" label="章节名称" />
            <el-table-column prop="subject" label="科目" />
            <el-table-column prop="status" label="状态" width="120">
              <template #default="scope">
                <el-tag :type="getStatusType(scope.row.status)" size="small">
                  {{ getStatusText(scope.row.status) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="score" label="得分" width="100" />
            <el-table-column prop="studyTime" label="学习时长（分钟）" width="150" />
            <el-table-column prop="lastStudyTime" label="最后学习时间" width="180">
              <template #default="scope">
                {{ formatDate(scope.row.lastStudyTime) }}
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useUserStore } from '../stores/user'
import { progressApi, chapterApi, wrongQuestionApi } from '../api'
import * as echarts from 'echarts'

const userStore = useUserStore()
const progressList = ref([])
const chapters = ref([])
const totalStudyTime = ref(0)
const completedChapters = ref(0)
const wrongCount = ref(0)

const progressChart = ref(null)
const timeChart = ref(null)
let progressChartInstance = null
let timeChartInstance = null

const getStatusType = (status) => {
  const map = { 'completed': 'success', 'in_progress': 'warning', 'not_started': 'info' }
  return map[status] || 'info'
}

const getStatusText = (status) => {
  const map = { 'completed': '已完成', 'in_progress': '学习中', 'not_started': '未开始' }
  return map[status] || status
}

const formatDate = (dateStr) => {
  if (!dateStr) return '-'
  const date = new Date(dateStr)
  return date.toLocaleString('zh-CN')
}

const initCharts = () => {
  // 各科学习进度饼图
  if (progressChart.value) {
    progressChartInstance = echarts.init(progressChart.value)
    const subjectProgress = {}
    chapters.value.forEach(ch => {
      if (!subjectProgress[ch.subject]) {
        subjectProgress[ch.subject] = { total: 0, completed: 0 }
      }
      subjectProgress[ch.subject].total++
      const progress = progressList.value.find(p => p.chapterId === ch.id)
      if (progress && progress.status === 'completed') {
        subjectProgress[ch.subject].completed++
      }
    })

    const pieData = Object.entries(subjectProgress).map(([name, data]) => ({
      name,
      value: data.total > 0 ? Math.round((data.completed / data.total) * 100) : 0
    }))

    progressChartInstance.setOption({
      tooltip: { trigger: 'item', formatter: '{b}: {c}%' },
      legend: { bottom: 0 },
      series: [{
        type: 'pie',
        radius: ['40%', '70%'],
        data: pieData,
        emphasis: {
          itemStyle: { shadowBlur: 10, shadowOffsetX: 0, shadowColor: 'rgba(0, 0, 0, 0.5)' }
        }
      }]
    })
  }

  // 学习时间柱状图
  if (timeChart.value) {
    timeChartInstance = echarts.init(timeChart.value)
    const subjectTime = {}
    progressList.value.forEach(p => {
      const chapter = chapters.value.find(c => c.id === p.chapterId)
      if (chapter) {
        if (!subjectTime[chapter.subject]) {
          subjectTime[chapter.subject] = 0
        }
        subjectTime[chapter.subject] += p.studyTime || 0
      }
    })

    const barData = Object.entries(subjectTime).map(([name, value]) => ({
      name,
      value: Math.round(value / 60)
    }))

    timeChartInstance.setOption({
      tooltip: { trigger: 'axis' },
      xAxis: {
        type: 'category',
        data: barData.map(d => d.name),
        axisLabel: { rotate: 30 }
      },
      yAxis: { type: 'value', name: '小时' },
      series: [{
        type: 'bar',
        data: barData.map(d => d.value),
        itemStyle: { color: '#409EFF' }
      }]
    })
  }
}

onMounted(async () => {
  try {
    const [progressRes, chapterRes, wrongRes] = await Promise.all([
      progressApi.getByUserId(userStore.userInfo.id),
      chapterApi.getAll(),
      wrongQuestionApi.getByUserId(userStore.userInfo.id)
    ])

    progressList.value = progressRes.data || []
    chapters.value = chapterRes.data || []
    wrongCount.value = (wrongRes.data || []).length

    // 计算统计数据
    progressList.value.forEach(p => {
      p.chapterName = chapters.value.find(c => c.id === p.chapterId)?.name || '未知'
      p.subject = chapters.value.find(c => c.id === p.chapterId)?.subject || '未知'
      totalStudyTime.value += p.studyTime || 0
      if (p.status === 'completed') {
        completedChapters.value++
      }
    })
    totalStudyTime.value = Math.round(totalStudyTime.value / 60)

    setTimeout(initCharts, 100)
  } catch (e) {
    console.error(e)
  }
})

onUnmounted(() => {
  if (progressChartInstance) {
    progressChartInstance.dispose()
  }
  if (timeChartInstance) {
    timeChartInstance.dispose()
  }
})
</script>

<style scoped>
.stat-item {
  text-align: center;
  padding: 20px 0;
}

.stat-value {
  font-size: 36px;
  font-weight: bold;
  color: #409EFF;
  margin-bottom: 8px;
}

.stat-label {
  font-size: 14px;
  color: #666;
}
</style>
