package com.ailearning.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Question {
    private Integer id;
    private Integer chapterId;
    private String type;
    private String content;
    private String options;
    private String answer;
    private String analysis;
    private Integer difficulty;
    private LocalDateTime createdAt;
}
