package com.communityfix.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class NotificationResponse {
    private Long id;
    private String message;
    private String type;
    private String link;
    private Boolean isRead;
    private LocalDateTime createdAt;
    private Long problemId;
    private Long meetingId;
}
