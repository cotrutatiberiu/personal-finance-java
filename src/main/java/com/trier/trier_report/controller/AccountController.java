package com.trier.trier_report.controller;

import com.trier.trier_report.dto.AccountArchiveRequest;
import com.trier.trier_report.dto.AccountResponse;
import com.trier.trier_report.dto.AccountUpdateRequest;
import com.trier.trier_report.dto.AccountCreateRequest;
import com.trier.trier_report.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody AccountCreateRequest request) {
        accountService.create(request);

        return ResponseEntity.status(201).build();
    }

    @PatchMapping
    public ResponseEntity<AccountResponse> update(@Valid @RequestBody AccountUpdateRequest request) {
        return ResponseEntity.ok(accountService.update(request));
    }

    @PatchMapping("/{id}/archive")
    public ResponseEntity<AccountResponse> archive(@PathVariable Long id, @Valid @RequestBody AccountArchiveRequest request) {
        return ResponseEntity.ok(accountService.archive(id, request));
    }
}
