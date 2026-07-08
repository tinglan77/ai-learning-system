package com.ailearning.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class WrongQuestion {
    private Integer id;
    private Integer userId;
    private Integer questionId;
    private String userAnswer;
    private Integer wrongCount;
    private LocalDateTime lastWrongTime;
    private String status;
    private String note;
    private LocalDateTime createdAt;
}
