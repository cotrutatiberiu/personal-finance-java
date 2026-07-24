package com.trier.trier_report.dto;

public record UserResponse(
        Long id,
        String firstName,
        String lastName,
        String email
) {
}
