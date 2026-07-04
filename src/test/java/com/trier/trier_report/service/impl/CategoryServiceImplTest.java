package com.trier.trier_report.service.impl;

import com.trier.trier_report.dao.CategoryRepository;
import com.trier.trier_report.dao.UserRepository;
import com.trier.trier_report.dto.CategoryCreateRequest;
import com.trier.trier_report.exception.DuplicateResourceException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    void create_Should_ReturnError_When_UserDoesNotExist() {
        Long userId = 1L;
        Long parentCategoryId = 1L;
        String name = "transportation";
        CategoryCreateRequest request = new CategoryCreateRequest(userId, parentCategoryId, name);

        when(userRepository.existsById(userId)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> {
            categoryService.create(request);
        });
    }

    @Test
    void create_Should_ReturnError_When_DuplicateCategory() {
        Long userId = 1L;
        Long parentCategoryId = null;
        String name = "transportation";
        CategoryCreateRequest request = new CategoryCreateRequest(userId, parentCategoryId, name);

        when(userRepository.existsById(userId)).thenReturn(true);
        when(categoryRepository.existsByUserIdAndNameIgnoreCase(request.userId(), request.name())).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> {
            categoryService.create(request);
        });
    }
}