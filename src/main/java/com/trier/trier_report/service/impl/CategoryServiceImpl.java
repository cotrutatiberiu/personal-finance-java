package com.trier.trier_report.service.impl;

import com.trier.trier_report.dao.CategoryRepository;
import com.trier.trier_report.dto.CategoryCreateRequest;
import com.trier.trier_report.entity.Category;
import com.trier.trier_report.exception.DuplicateResourceException;
import com.trier.trier_report.mapper.CategoryMapper;
import com.trier.trier_report.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void create(CategoryCreateRequest categoryCreateRequest) {
        Category category = CategoryMapper.toEntity(categoryCreateRequest);

        // Parent category
        if (category.getParentCategoryId() == null) {
            if (categoryRepository.existsByUserIdAndNameIgnoreCase(category.getUserId(), category.getName())) {
                throw new DuplicateResourceException("Duplicate category");
            }
            categoryRepository.save(category);
        }
        // Subcategory
//        else {
//            if(categoryRepository.)
//            if (categoryRepository.existsByUserIdAndParentCategoryIdAndNameIgnoreCase(category.getUserId(), category.getParentCategoryId(), category.getName())) {
//                throw new DuplicateResourceException("Duplicate subcategory");
//            }
//            categoryRepository.save(category);
//        }
    }
}
