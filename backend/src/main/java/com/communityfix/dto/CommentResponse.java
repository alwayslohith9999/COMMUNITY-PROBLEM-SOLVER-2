package com.communityfix.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class CommentResponse {
    private Long id;
    private String content;
    private Boolean isVolunteerOffer;
    private Boolean isSolution;
    private Integer upvoteCount;
    private LocalDateTime createdAt;
    private UserSummary user;
    private List<CommentResponse> replies;
}
