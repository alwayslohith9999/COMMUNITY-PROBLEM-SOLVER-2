package com.communityfix.dto;

import com.communityfix.model.enums.Category;
import com.communityfix.model.enums.ProblemStatus;
import com.communityfix.model.enums.UrgencyLevel;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ProblemResponse {
    private Long id;
    private String title;
    private String description;
    private Category category;
    private UrgencyLevel urgency;
    private ProblemStatus status;
    private String block;
    private String flatNumber;
    private String photoUrl;
    private Integer upvoteCount;
    private Integer commentCount;
    private Integer volunteerCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime resolvedAt;
    private UserSummary postedBy;
    private UserSummary assignedTo;
    private UserSummary resolvedBy;
    private Boolean userHasUpvoted;
    private Boolean userIsVolunteer;
}
