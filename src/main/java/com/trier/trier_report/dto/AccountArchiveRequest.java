package com.trier.trier_report.dto;

import jakarta.validation.constraints.NotNull;

public record AccountArchiveRequest(
        @NotNull
        Boolean archived
) {
}
