package com.communityfix.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiError {
    private boolean error;
    private String message;
    private int status;
}
