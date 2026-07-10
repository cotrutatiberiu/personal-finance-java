package com.trier.trier_report.service.impl;

import com.trier.trier_report.dao.CategoryRepository;
import com.trier.trier_report.dao.UserRepository;
import com.trier.trier_report.dto.CategoryCreateRequest;
import com.trier.trier_report.dto.CategoryResponse;
import com.trier.trier_report.entity.Category;
import com.trier.trier_report.exception.DuplicateResourceException;
import com.trier.trier_report.service.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
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

        // ACT & ASSERT ===
        assertThrows(EntityNotFoundException.class, () -> {
            categoryService.create(request);
        });

        // VERIFY
        verify(userRepository, times(1)).existsById(userId);
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    void create_Should_ThrowDuplicateResourceException_When_ParentCategoryAlreadyExists() {
        // ARRANGE
        Long userId = 1L;
        Long parentCategoryId = null;
        String name = "transportation";
        CategoryCreateRequest request = new CategoryCreateRequest(userId, parentCategoryId, name);

        when(userRepository.existsById(userId)).thenReturn(true);
        when(categoryRepository.existsByUserIdAndNameIgnoreCase(request.userId(), request.name())).thenReturn(true);

        // ACT & ASSERT
        assertThrows(DuplicateResourceException.class, () -> {
            categoryService.create(request);
        });

        // VERIFY
        verify(userRepository, times(1)).existsById(userId);
        verify(categoryRepository, times(1)).existsByUserIdAndNameIgnoreCase(userId, name);
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    void create_Should_ReturnParentCategoryResponse_When_CategoryDoesNotExist() {
        // Arrange
        Long userId = 1L;
        Long parentCategoryId = null;
        String name = "transportation";
        CategoryCreateRequest request = new CategoryCreateRequest(userId, parentCategoryId, name);
        Category savedCategory = new Category(userId, parentCategoryId, name);

        when(userRepository.existsById(userId)).thenReturn(true);
        when(categoryRepository.existsByUserIdAndNameIgnoreCase(request.userId(), request.name())).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(savedCategory);

        // Act
        CategoryResponse response = categoryService.create(request);

        // Assert
        assertEquals(name, response.name());
        assertNotNull(response);

        // Verify
        verify(userRepository, times(1)).existsById(userId);
        verify(categoryRepository, times(1)).existsByUserIdAndNameIgnoreCase(userId, name);
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void create_Should_ThrowDuplicateResourceException_When_SubCategoryAlreadyExists() {
        // ARRANGE
        Long userId = 1L;
        Long parentCategoryId = 1L;
        String name = "transportation";
        CategoryCreateRequest request = new CategoryCreateRequest(userId, parentCategoryId, name);

        when(userRepository.existsById(userId)).thenReturn(true);
        when(categoryRepository.existsByUserIdAndParentCategoryIdAndNameIgnoreCase(userId, parentCategoryId, name)).thenReturn(true);

        // ACT & ASSERT
        assertThrows(DuplicateResourceException.class, () -> {
            categoryService.create(request);
        });

        // VERIFY
        verify(userRepository, times(1)).existsById(userId);
        verify(categoryRepository, times(1)).existsByUserIdAndParentCategoryIdAndNameIgnoreCase(userId, parentCategoryId, name);
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    void create_Should_ReturnSubCategoryResponse_When_CategoryDoesNotExist() {
        // Arrange
        Long userId = 1L;
        Long parentCategoryId = 1L;
        String name = "transportation";
        CategoryCreateRequest request = new CategoryCreateRequest(userId, parentCategoryId, name);
        Category savedCategory = new Category(userId, parentCategoryId, name);

        when(userRepository.existsById(userId)).thenReturn(true);
        when(categoryRepository.existsByUserIdAndParentCategoryIdAndNameIgnoreCase(userId, parentCategoryId, name)).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(savedCategory);

        // Act
        CategoryResponse response = categoryService.create(request);

        // Assert
        assertEquals(name, response.name());
        assertEquals(parentCategoryId, response.parentCategoryId());
        assertNotNull(response);

        // Verify
        verify(userRepository, times(1)).existsById(userId);
        verify(categoryRepository, times(1)).existsByUserIdAndParentCategoryIdAndNameIgnoreCase(userId, parentCategoryId, name);
        verify(categoryRepository, times(1)).save(any(Category.class));
    }
}