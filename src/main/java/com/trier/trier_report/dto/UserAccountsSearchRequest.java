package com.trier.trier_report.dto;

import com.trier.trier_report.enums.AccountSortField;
import jakarta.validation.constraints.Min;

public record UserAccountsSearchRequest(
        @Min(value = 1, message = "Page number must be 1 or greater")
        int pageSize,
        @Min(value = 1, message = "Page size must be at least 1")
        int pageNumber,
        AccountSortField sortBy
) {
}
