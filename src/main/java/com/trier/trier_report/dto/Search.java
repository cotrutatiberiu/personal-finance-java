package com.trier.trier_report.dto;

import jakarta.validation.constraints.Min;

public record Search(
        @Min(value = 1, message = "Page number must be 1 or greater")
        int pageSize,
        @Min(value = 1, message = "Page size must be at least 1")
        int pageNumber,
        String sortBy
) {
}
