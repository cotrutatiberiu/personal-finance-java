package com.trier.trier_report.dto;

import jakarta.validation.Valid;

public record UserCategoriesSearchRequest(
        @Valid
        Search search,
        String parentCategoryMame
) {
}
