package com.communityfix.dto;

import com.communityfix.model.enums.UserRole;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private Long userId;
    private String fullName;
    private String email;
    private UserRole role;
    private String flatNumber;
    private String block;
    private Integer reputation;
    private Boolean approved;
}
