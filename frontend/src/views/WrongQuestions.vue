<template>
  <div class="wrong-questions">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>错题本</span>
          <el-button type="primary" size="small" @click="refresh">刷新</el-button>
        </div>
      </template>

      <el-table :data="wrongQuestions" style="width: 100%">
        <el-table-column prop="questionContent" label="题目内容" show-overflow-tooltip />
        <el-table-column prop="userAnswer" label="你的答案" width="150" />
        <el-table-column prop="correctAnswer" label="正确答案" width="150" />
        <el-table-column prop="wrongCount" label="错误次数" width="100" />
        <el-table-column prop="lastWrongTime" label="最后错误时间" width="180">
          <template #default="scope">
            {{ formatDate(scope.row.lastWrongTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="scope">
            <el-button type="primary" size="small" @click="viewDetail(scope.row)">查看详情</el-button>
            <el-button type="success" size="small" @click="markCorrect(scope.row.id)">标记正确</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-empty v-if="wrongQuestions.length === 0" description="暂无错题" />
    </el-card>

    <el-dialog v-model="dialogVisible" title="题目详情" width="600px">
      <div v-if="currentQuestion">
        <h4>题目内容</h4>
        <div class="question-content" v-html="formatContent(currentQuestion.questionContent)"></div>
        
        <el-divider />
        
        <h4>你的答案</h4>
        <p class="wrong-answer">{{ currentQuestion.userAnswer || '未作答' }}</p>
        
        <h4>正确答案</h4>
        <p class="correct-answer">{{ currentQuestion.correctAnswer }}</p>
        
        <h4>解析</h4>
        <div class="analysis" v-html="formatContent(currentQuestion.analysis || '暂无解析')"></div>
        
        <h4>笔记</h4>
        <el-input
          v-model="currentQuestion.note"
          type="textarea"
          :rows="3"
          placeholder="添加你的笔记..."
        />
        
        <div style="margin-top: 20px; text-align: right">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="saveNote">保存笔记</el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useUserStore } from '../stores/user'
import { wrongQuestionApi } from '../api'
import { ElMessage } from 'element-plus'

const userStore = useUserStore()
const wrongQuestions = ref([])
const dialogVisible = ref(false)
const currentQuestion = ref(null)

const formatDate = (dateStr) => {
  if (!dateStr) return '-'
  const date = new Date(dateStr)
  return date.toLocaleString('zh-CN')
}

const formatContent = (content) => {
  return content.replace(/\n/g, '<br>')
}

const refresh = async () => {
  try {
    const res = await wrongQuestionApi.getByUserId(userStore.userInfo.id)
    wrongQuestions.value = res.data || []
  } catch (e) {
    console.error(e)
  }
}

const viewDetail = (row) => {
  currentQuestion.value = row
  dialogVisible.value = true
}

const markCorrect = async (id) => {
  try {
    await wrongQuestionApi.markAsCorrect(id)
    ElMessage.success('已标记为正确')
    await refresh()
  } catch (e) {
    console.error(e)
  }
}

const saveNote = async () => {
  try {
    await wrongQuestionApi.update(userStore.userInfo.id, currentQuestion.value.id, {
      note: currentQuestion.value.note
    })
    ElMessage.success('笔记已保存')
    dialogVisible.value = false
  } catch (e) {
    console.error(e)
  }
}

onMounted(() => {
  refresh()
})
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.question-content {
  line-height: 1.8;
  margin-bottom: 16px;
}

.wrong-answer {
  color: #f56c6c;
  font-weight: bold;
}

.correct-answer {
  color: #67c23a;
  font-weight: bold;
}

.analysis {
  background: #f5f7fa;
  padding: 12px;
  border-radius: 6px;
  line-height: 1.8;
}
</style>
