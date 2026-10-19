package com.communityfix.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSummary {
    private Long id;
    private String fullName;
    private String email;
    private String flatNumber;
    private String block;
    private String phoneNumber;
    private Integer reputation;
    private String reputationRank;
    private String avatarInitials;
}
