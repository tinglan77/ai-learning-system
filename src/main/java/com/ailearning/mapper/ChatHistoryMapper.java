package com.ailearning.mapper;

import com.ailearning.entity.ChatHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChatHistoryMapper {
    ChatHistory findById(@Param("id") Integer id);
    List<ChatHistory> findByUserId(@Param("userId") Integer userId);
    int insert(ChatHistory chatHistory);
    int deleteByUserId(@Param("userId") Integer userId);
}
