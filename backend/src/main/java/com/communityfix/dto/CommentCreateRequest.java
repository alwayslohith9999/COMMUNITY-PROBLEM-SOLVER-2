package com.communityfix.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentCreateRequest {
    @NotBlank(message = "Comment content is required")
    @Size(max = 2000, message = "Comment must be less than 2000 characters")
    private String content;

    private Boolean isVolunteerOffer = false;
    private Long parentId;
}
