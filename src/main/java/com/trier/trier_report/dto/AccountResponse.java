package com.trier.trier_report.dto;

import java.time.Instant;

public record AccountResponse(
        Long id,
        Long userId,
        Long currencyId,
        String name,
        boolean archived,
        Instant createdAt,
        Instant updatedAt
) {
    @Override
    public Long id() {
        return id;
    }

    @Override
    public Long userId() {
        return userId;
    }

    @Override
    public Long currencyId() {
        return currencyId;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public boolean archived() {
        return archived;
    }

    @Override
    public Instant createdAt() {
        return createdAt;
    }

    @Override
    public Instant updatedAt() {
        return updatedAt;
    }
}
