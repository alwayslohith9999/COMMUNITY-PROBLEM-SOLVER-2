package com.communityfix.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MeetingCreateRequest {
    @NotBlank(message = "Meeting title is required")
    private String title;

    private Long problemId;
    private String description;

    @NotNull(message = "Schedule date and time is required")
    private LocalDateTime scheduledAt;

    private Boolean notifyAll = false;
}
