package com.trier.trier_report.security;

import com.trier.trier_report.dao.CategoryRepository;
import com.trier.trier_report.dto.CategoryCreateRequest;
import com.trier.trier_report.entity.CustomUserDetails;
import org.springframework.security.core.Authentication;

public class CategorySecurity {
    private final CategoryRepository categoryRepository;

    public CategorySecurity(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public boolean isCategoryOwner(CategoryCreateRequest payload, Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return false;
        }

        if (payload.parentCategoryId() == null) {
            return true;
        }

        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        Long currentUserId = userDetails.getId();

        return categoryRepository.findByUserId(payload.userId()).map(category -> category.getUserId().equals(currentUserId)).orElse(false);
    }
}
