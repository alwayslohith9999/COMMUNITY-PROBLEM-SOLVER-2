package com.communityfix.dto;

import com.communityfix.model.enums.Category;
import com.communityfix.model.enums.UrgencyLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProblemCreateRequest {
    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title must be less than 200 characters")
    private String title;

    @NotNull(message = "Category is required")
    private Category category;

    @NotNull(message = "Urgency level is required")
    private UrgencyLevel urgency;

    @NotBlank(message = "Description is required")
    @Size(max = 2000, message = "Description must be less than 2000 characters")
    private String description;

    @NotBlank(message = "Block is required")
    private String block;

    @NotBlank(message = "Flat number is required")
    private String flatNumber;

    private String photoUrl;
    private Boolean notifyNeighbors = false;
}
