package com.trier.trier_report.mapper;

import com.trier.trier_report.dto.CategoryCreateRequest;
import com.trier.trier_report.dto.CategoryResponse;
import com.trier.trier_report.entity.Category;

public class CategoryMapper {
    public static Category toEntity(CategoryCreateRequest payload) {
        return new Category(payload.userId(), payload.parentCategoryId(), payload.name());
    }

    public static CategoryResponse toDto(Category category) {
        return new CategoryResponse(category.getId(), category.getUserId(), category.getParentCategoryId(), category.getName(), category.getCreatedAt(), category.getUpdatedAt());
    }
}
