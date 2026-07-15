package com.trier.trier_report.controller;

import com.trier.trier_report.dao.CategoryRepository;
import com.trier.trier_report.dto.CategoryCreateRequest;
import com.trier.trier_report.dto.CategoryResponse;
import com.trier.trier_report.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    @PreAuthorize("@categorySecurity.isCategoryOwner(#payload, authentication")
    public ResponseEntity<CategoryResponse> create(@RequestBody CategoryCreateRequest request) {
        CategoryResponse categoryResponse = categoryService.create(request);

        return ResponseEntity.ok(categoryResponse);
    }
}
