package com.communityfix.dto;

import com.communityfix.model.enums.ProblemStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StatusUpdateRequest {
    @NotNull(message = "Status is required")
    private ProblemStatus status;
}
