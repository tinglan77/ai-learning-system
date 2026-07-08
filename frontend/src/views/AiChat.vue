<template>
  <div class="ai-chat">
    <el-card class="chat-card">
      <template #header>
        <div class="card-header">
          <span>AI 答疑助手</span>
          <el-button type="danger" size="small" @click="clearChat">清空对话</el-button>
        </div>
      </template>

      <div class="chat-container" ref="chatContainer">
        <div v-for="(msg, index) in messages" :key="index" :class="['message', msg.role]">
          <div class="message-content">
            <div class="message-avatar">
              <el-icon v-if="msg.role === 'user'" :size="20"><User /></el-icon>
              <el-icon v-else :size="20"><ChatDotRound /></el-icon>
            </div>
            <div class="message-text" v-html="formatMessage(msg.content)"></div>
          </div>
        </div>
        <div v-if="loading" class="message assistant">
          <div class="message-content">
            <div class="message-avatar">
              <el-icon :size="20"><ChatDotRound /></el-icon>
            </div>
            <div class="message-text">正在思考中...</div>
          </div>
        </div>
      </div>

      <div class="input-area">
        <el-input
          v-model="inputMessage"
          type="textarea"
          :rows="3"
          placeholder="输入你的问题，例如：请解释二叉树的遍历方式..."
          @keydown.enter.exact="sendMessage"
        />
        <el-button type="primary" @click="sendMessage" :loading="loading" style="margin-top: 10px">
          发送
        </el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import { useUserStore } from '../stores/user'
import { aiApi, chatApi } from '../api'
import { ElMessage } from 'element-plus'

const userStore = useUserStore()
const messages = ref([])
const inputMessage = ref('')
const loading = ref(false)
const chatContainer = ref(null)

const formatMessage = (content) => {
  return content.replace(/\n/g, '<br>')
}

const scrollToBottom = async () => {
  await nextTick()
  if (chatContainer.value) {
    chatContainer.value.scrollTop = chatContainer.value.scrollHeight
  }
}

const sendMessage = async () => {
  if (!inputMessage.value.trim()) {
    ElMessage.warning('请输入问题')
    return
  }

  const question = inputMessage.value.trim()
  messages.value.push({ role: 'user', content: question })
  inputMessage.value = ''
  loading.value = true
  await scrollToBottom()

  try {
    const res = await aiApi.chat({
      userId: userStore.userInfo.id,
      question: question
    })
    messages.value.push({ role: 'assistant', content: res.data.answer })
  } catch (e) {
    messages.value.push({ role: 'assistant', content: '抱歉，服务暂时不可用，请稍后再试。' })
  } finally {
    loading.value = false
    await scrollToBottom()
  }
}

const clearChat = async () => {
  try {
    await chatApi.clearHistory(userStore.userInfo.id)
    messages.value = []
    ElMessage.success('对话已清空')
  } catch (e) {
    console.error(e)
  }
}

onMounted(async () => {
  try {
    const res = await chatApi.getHistory(userStore.userInfo.id)
    messages.value = res.data || []
    await scrollToBottom()
  } catch (e) {
    console.error(e)
  }
})
</script>

<style scoped>
.chat-card {
  height: calc(100vh - 120px);
  display: flex;
  flex-direction: column;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.chat-container {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  background: #f9f9f9;
  border-radius: 8px;
  margin-bottom: 20px;
}

.message {
  margin-bottom: 20px;
}

.message.user {
  text-align: right;
}

.message-content {
  display: inline-flex;
  align-items: flex-start;
  gap: 10px;
  max-width: 70%;
}

.message.user .message-content {
  flex-direction: row-reverse;
}

.message-avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background: #409EFF;
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.message.user .message-avatar {
  background: #67C23A;
}

.message-text {
  padding: 12px 16px;
  border-radius: 12px;
  background: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  line-height: 1.6;
  text-align: left;
}

.message.user .message-text {
  background: #409EFF;
  color: #fff;
}

.input-area {
  padding: 16px;
  border-top: 1px solid #eee;
}
</style>
