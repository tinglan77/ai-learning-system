package com.ailearning.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class WrongQuestionDetail {
    private Integer id;
    private Integer userId;
    private Integer questionId;
    private String questionContent;
    private String options;
    private String userAnswer;
    private String correctAnswer;
    private String analysis;
    private Integer wrongCount;
    private LocalDateTime lastWrongTime;
    private String status;
    private String note;
    private LocalDateTime createdAt;
}
