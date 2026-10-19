package com.communityfix.dto;

import com.communityfix.model.enums.MeetingStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class MeetingResponse {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime scheduledAt;
    private String jitsiRoom;
    private MeetingStatus status;
    private UserSummary host;
    private ProblemSummary problem;
    private List<UserSummary> attendees;
    private Integer attendeeCount;
    private Boolean isUserAttending;
    private Boolean isLive;
}
