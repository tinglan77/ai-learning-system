package com.ailearning.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ChatHistory {
    private Integer id;
    private Integer userId;
    private String role;
    private String content;
    private LocalDateTime createdAt;
}
