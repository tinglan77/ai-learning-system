package com.ailearning.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Chapter {
    private Integer id;
    private String subject;
    private String name;
    private String description;
    private Integer difficulty;
    private Integer sortOrder;
    private LocalDateTime createdAt;
}
