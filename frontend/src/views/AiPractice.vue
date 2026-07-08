<template>
  <div class="ai-practice">
    <el-card>
      <template #header>
        <span>AI 练习生成</span>
      </template>

      <el-form :model="form" label-width="100px" style="max-width: 600px">
        <el-form-item label="科目">
          <el-select v-model="form.subject" placeholder="选择科目">
            <el-option label="数据结构" value="数据结构" />
            <el-option label="计算机组成原理" value="计算机组成原理" />
            <el-option label="操作系统" value="操作系统" />
            <el-option label="计算机网络" value="计算机网络" />
          </el-select>
        </el-form-item>
        <el-form-item label="章节">
          <el-select v-model="form.chapter" placeholder="选择章节">
            <el-option v-for="ch in filteredChapters" :key="ch.id" :label="ch.name" :value="ch.name" />
          </el-select>
        </el-form-item>
        <el-form-item label="题目类型">
          <el-radio-group v-model="form.type">
            <el-radio label="choice">选择题</el-radio>
            <el-radio label="short_answer">简答题</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="难度">
          <el-rate v-model="form.difficulty" :max="5" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="generateQuestion" :loading="loading">生成题目</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card v-if="generatedQuestion" style="margin-top: 20px">
      <template #header>
        <div class="card-header">
          <span>生成的题目</span>
          <el-button type="success" size="small" @click="showAnswer = !showAnswer">
            {{ showAnswer ? '隐藏答案' : '显示答案' }}
          </el-button>
        </div>
      </template>
      <div class="question-content" v-html="formatContent(generatedQuestion)"></div>
      <div v-if="showAnswer" class="answer-section">
        <el-divider />
        <h4>答案与解析</h4>
        <div v-html="formatContent(generatedQuestion)"></div>
      </div>
      <div style="margin-top: 20px">
        <el-button type="primary" @click="addToWrongBook">加入错题本</el-button>
        <el-button @click="generateQuestion">再生成一题</el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useUserStore } from '../stores/user'
import { aiApi, chapterApi, wrongQuestionApi } from '../api'
import { ElMessage } from 'element-plus'

const userStore = useUserStore()
const chapters = ref([])
const loading = ref(false)
const generatedQuestion = ref('')
const showAnswer = ref(false)

const form = reactive({
  subject: '',
  chapter: '',
  type: 'choice',
  difficulty: 3
})

const filteredChapters = computed(() => {
  if (!form.subject) return chapters.value
  return chapters.value.filter(c => c.subject === form.subject)
})

const formatContent = (content) => {
  return content.replace(/\n/g, '<br>')
}

const generateQuestion = async () => {
  if (!form.subject || !form.chapter) {
    ElMessage.warning('请选择科目和章节')
    return
  }

  loading.value = true
  showAnswer.value = false
  try {
    const res = await aiApi.generateQuestion({
      subject: form.subject,
      chapter: form.chapter,
      type: form.type,
      difficulty: form.difficulty
    })
    generatedQuestion.value = res.data.content
    ElMessage.success('题目生成成功')
  } catch (e) {
    ElMessage.error('生成失败，请稍后重试')
  } finally {
    loading.value = false
  }
}

const addToWrongBook = async () => {
  try {
    await wrongQuestionApi.add(userStore.userInfo.id, 0, '', 'AI生成题目')
    ElMessage.success('已加入错题本')
  } catch (e) {
    console.error(e)
  }
}

onMounted(async () => {
  try {
    const res = await chapterApi.getAll()
    chapters.value = res.data || []
  } catch (e) {
    console.error(e)
  }
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
  font-size: 15px;
}

.answer-section {
  background: #f0f9ff;
  padding: 16px;
  border-radius: 8px;
  margin-top: 16px;
}
</style>
