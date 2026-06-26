package com.trier.trier_report.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CategoryCreateRequest(
        @NotNull
        Long userId,
        Long parentCategoryId,
        @Size(min = 3, max = 20, message = "Name should be at least 3 characters long")
        String name
) {
}
