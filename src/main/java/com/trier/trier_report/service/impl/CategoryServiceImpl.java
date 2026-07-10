package com.trier.trier_report.service.impl;

import com.trier.trier_report.dao.CategoryRepository;
import com.trier.trier_report.dao.UserRepository;
import com.trier.trier_report.dto.CategoryCreateRequest;
import com.trier.trier_report.dto.CategoryResponse;
import com.trier.trier_report.entity.Category;
import com.trier.trier_report.exception.DuplicateResourceException;
import com.trier.trier_report.mapper.CategoryMapper;
import com.trier.trier_report.service.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    @Override
    public CategoryResponse create(CategoryCreateRequest categoryCreateRequest) {
        Category category = CategoryMapper.toEntity(categoryCreateRequest);
        if (!userRepository.existsById(category.getUserId())) {
            throw new EntityNotFoundException("User not found");
        }

        if (categoryCreateRequest.name() != null) {
            // Parent category
            if (category.getParentCategoryId() == null) {
                if (categoryRepository.existsByUserIdAndNameIgnoreCase(category.getUserId(), category.getName())) {
                    throw new DuplicateResourceException("Duplicate category");
                }
            } else {
                // Subcategory
                if (categoryRepository.existsByUserIdAndParentCategoryIdAndNameIgnoreCase(category.getUserId(), category.getParentCategoryId(), category.getName())) {
                    throw new DuplicateResourceException("Duplicate subcategory");
                }
            }
        }

        Category savedCategory = categoryRepository.save(category);
        return CategoryMapper.toDto(savedCategory);
    }
}
