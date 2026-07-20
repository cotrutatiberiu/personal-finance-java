package com.trier.trier_report.service;

import com.trier.trier_report.dto.CategoryCreateRequest;
import com.trier.trier_report.dto.CategoryResponse;
import com.trier.trier_report.dto.PaginatedResponse;
import com.trier.trier_report.dto.UserCategoriesSearchRequest;

public interface CategoryService {
    CategoryResponse create(CategoryCreateRequest categoryCreateRequest);

    PaginatedResponse<CategoryResponse> findCategoriesByUserId(Long userId, UserCategoriesSearchRequest request);
}
