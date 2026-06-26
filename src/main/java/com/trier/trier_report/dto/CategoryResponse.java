package com.trier.trier_report.dto;

import java.time.Instant;

public record CategoryResponse (
    Long id,
    Long userId,
    Long parentCategoryId,
    String name,
    Instant createdAt,
    Instant updatedAt
){
    @Override
    public Long id() {
        return id;
    }

    @Override
    public Long userId() {
        return userId;
    }

    @Override
    public Long parentCategoryId() {
        return parentCategoryId;
    }

    @Override
    public String name() {
        return name;
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
