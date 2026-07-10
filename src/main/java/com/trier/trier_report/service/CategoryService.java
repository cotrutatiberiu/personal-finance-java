package com.trier.trier_report.service;

import com.trier.trier_report.dto.CategoryCreateRequest;
import com.trier.trier_report.dto.CategoryResponse;

public interface CategoryService {
    CategoryResponse create(CategoryCreateRequest categoryCreateRequest);
}
