package com.ailearning.service;

import com.ailearning.entity.ChatHistory;
import com.ailearning.mapper.ChatHistoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatHistoryService {

    @Autowired
    private ChatHistoryMapper chatHistoryMapper;

    public List<ChatHistory> findByUserId(Integer userId) {
        return chatHistoryMapper.findByUserId(userId);
    }

    public boolean saveMessage(Integer userId, String role, String content) {
        ChatHistory chatHistory = new ChatHistory();
        chatHistory.setUserId(userId);
        chatHistory.setRole(role);
        chatHistory.setContent(content);
        return chatHistoryMapper.insert(chatHistory) > 0;
    }

    public boolean clearHistory(Integer userId) {
        return chatHistoryMapper.deleteByUserId(userId) > 0;
    }
}
