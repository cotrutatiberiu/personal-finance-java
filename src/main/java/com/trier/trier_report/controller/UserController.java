package com.trier.trier_report.controller;

import com.trier.trier_report.dto.*;
import com.trier.trier_report.service.AccountService;
import com.trier.trier_report.service.CategoryService;
import com.trier.trier_report.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final AccountService accountService;
    private final CategoryService categoryService;

    @Autowired
    public UserController(AccountService accountService, CategoryService categoryService) {
        this.accountService = accountService;
        this.categoryService = categoryService;
    }

    @PostMapping("/{userId}/accounts/search")
    public ResponseEntity<PaginatedResponse<AccountResponse>> getUserAccounts(@PathVariable Long userId, @Valid @RequestBody UserAccountsSearchRequest request) {
        return ResponseEntity.ok(accountService.findAccountsByUserId(userId, request));
    }

    @GetMapping("/{userId}/accounts/{accountId}")
    public ResponseEntity<AccountResponse> getUserAccount(@PathVariable Long userId, @PathVariable Long accountId) {
        return ResponseEntity.ok(accountService.findAccountByUserId(userId, accountId));
    }

    @PostMapping("/{userId}/categories/search")
    public ResponseEntity<PaginatedResponse<CategoryResponse>> getUserParentCategories(@PathVariable Long userId, @Valid @RequestBody UserCategoriesSearchRequest request) {
        return ResponseEntity.ok(categoryService.findParentCategoriesByUserId(userId, request));
    }
}
