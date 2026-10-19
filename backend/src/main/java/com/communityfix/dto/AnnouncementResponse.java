package com.communityfix.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AnnouncementResponse {
    private Long id;
    private String title;
    private String content;
    private Boolean pinned;
    private Integer thumbsUpCount;
    private Integer questionCount;
    private Integer checkCount;
    private LocalDateTime createdAt;
    private UserSummary postedBy;
}
