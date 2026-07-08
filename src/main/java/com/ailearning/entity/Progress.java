package com.ailearning.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Progress {
    private Integer id;
    private Integer userId;
    private Integer chapterId;
    private String status;
    private Integer score;
    private Integer studyTime;
    private LocalDateTime lastStudyTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
