package com.ailearning.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Resource {
    private Integer id;
    private Integer chapterId;
    private String title;
    private String type;
    private String url;
    private String description;
    private Integer difficulty;
    private Integer viewCount;
    private LocalDateTime createdAt;
}
