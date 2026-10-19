package com.communityfix.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProblemSummary {
    private Long id;
    private String title;
    private String category;
}
