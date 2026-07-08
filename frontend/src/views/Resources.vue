<template>
  <div class="resources">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>资源中心</span>
          <el-select v-model="selectedSubject" placeholder="选择科目" clearable @change="filterResources">
            <el-option label="全部" value="" />
            <el-option label="数据结构" value="数据结构" />
            <el-option label="计算机组成原理" value="计算机组成原理" />
            <el-option label="操作系统" value="操作系统" />
            <el-option label="计算机网络" value="计算机网络" />
          </el-select>
        </div>
      </template>

      <el-table :data="filteredResources" style="width: 100%">
        <el-table-column prop="title" label="资源名称" />
        <el-table-column prop="type" label="类型" width="100">
          <template #default="scope">
            <el-tag :type="getTypeTag(scope.row.type)" size="small">{{ scope.row.type }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="chapterName" label="所属章节" />
        <el-table-column prop="difficulty" label="难度" width="100">
          <template #default="scope">
            <el-rate v-model="scope.row.difficulty" disabled :max="5" size="small" />
          </template>
        </el-table-column>
        <el-table-column prop="viewCount" label="浏览量" width="100" />
        <el-table-column label="操作" width="120">
          <template #default="scope">
            <el-button type="primary" size="small" @click="viewResource(scope.row)">查看</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" title="资源详情" width="500px">
      <div v-if="currentResource">
        <h3>{{ currentResource.title }}</h3>
        <p><strong>类型：</strong>{{ currentResource.type }}</p>
        <p><strong>描述：</strong>{{ currentResource.description }}</p>
        <p><strong>链接：</strong><el-link type="primary" :href="currentResource.url" target="_blank">{{ currentResource.url }}</el-link></p>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { resourceApi, chapterApi } from '../api'

const selectedSubject = ref('')
const resources = ref([])
const chapters = ref([])
const filteredResources = ref([])
const dialogVisible = ref(false)
const currentResource = ref(null)

const getTypeTag = (type) => {
  const map = { 'video': 'primary', 'article': 'success', 'exercise': 'warning' }
  return map[type] || 'info'
}

const filterResources = () => {
  if (!selectedSubject.value) {
    filteredResources.value = resources.value
  } else {
    const chapterIds = chapters.value
      .filter(c => c.subject === selectedSubject.value)
      .map(c => c.id)
    filteredResources.value = resources.value.filter(r => chapterIds.includes(r.chapterId))
  }
}

const viewResource = (resource) => {
  currentResource.value = resource
  dialogVisible.value = true
}

onMounted(async () => {
  try {
    const [resRes, chapRes] = await Promise.all([
      resourceApi.getAll(),
      chapterApi.getAll()
    ])
    resources.value = resRes.data || []
    chapters.value = chapRes.data || []
    
    resources.value.forEach(r => {
      const chapter = chapters.value.find(c => c.id === r.chapterId)
      r.chapterName = chapter ? chapter.name : '未分类'
    })
    
    filteredResources.value = resources.value
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
</style>
