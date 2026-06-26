package com.trier.trier_report.controller;

import com.trier.trier_report.dao.CategoryRepository;
import com.trier.trier_report.dto.CategoryCreateRequest;
import com.trier.trier_report.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Void> create(@Valid @RequestBody CategoryCreateRequest request) {
        categoryService.create(request);

        return ResponseEntity.status(201).build();
    }
}
