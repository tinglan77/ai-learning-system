import request from './request'

export const userApi = {
  login(data) {
    return request.post('/user/login', data)
  },
  register(data) {
    return request.post('/user/register', data)
  },
  getUserInfo(id) {
    return request.get(`/user/info/${id}`)
  },
  updateUser(data) {
    return request.put('/user/update', data)
  }
}

export const chapterApi = {
  getAll() {
    return request.get('/chapter/list')
  },
  getBySubject(subject) {
    return request.get(`/chapter/subject/${subject}`)
  },
  getById(id) {
    return request.get(`/chapter/${id}`)
  }
}

export const progressApi = {
  getByUserId(userId) {
    return request.get(`/progress/user/${userId}`)
  },
  update(data) {
    return request.post('/progress/update', data)
  },
  updateStudyTime(userId, chapterId, studyTime) {
    return request.post('/progress/study-time', null, {
      params: { userId, chapterId, studyTime }
    })
  }
}

export const resourceApi = {
  getAll() {
    return request.get('/resource/list')
  },
  getByChapterId(chapterId) {
    return request.get(`/resource/chapter/${chapterId}`)
  },
  getById(id) {
    return request.get(`/resource/${id}`)
  }
}

export const questionApi = {
  getAll() {
    return request.get('/question/list')
  },
  getByChapterId(chapterId) {
    return request.get(`/question/chapter/${chapterId}`)
  },
  getById(id) {
    return request.get(`/question/${id}`)
  }
}

export const wrongQuestionApi = {
  getByUserId(userId) {
    return request.get(`/wrong-question/user/${userId}`)
  },
  add(userId, questionId, userAnswer, note) {
    return request.post('/wrong-question/add', null, {
      params: { userId, questionId, userAnswer, note }
    })
  },
  markAsCorrect(id) {
    return request.put(`/wrong-question/correct/${id}`)
  },
  delete(id) {
    return request.delete(`/wrong-question/delete/${id}`)
  }
}

export const aiApi = {
  chat(data) {
    return request.post('/ai/chat', data)
  },
  generateQuestion(data) {
    return request.post('/ai/generate-question', data)
  },
  recommendResources(data) {
    return request.post('/ai/recommend', data)
  }
}

export const chatApi = {
  getHistory(userId) {
    return request.get(`/chat/history/${userId}`)
  },
  saveMessage(userId, role, content) {
    return request.post('/chat/save', null, {
      params: { userId, role, content }
    })
  },
  clearHistory(userId) {
    return request.delete(`/chat/clear/${userId}`)
  }
}
