package com.trier.trier_report.dto;

public record LoginResult(
        String accessToken,
        String refreshToken
) {
}
