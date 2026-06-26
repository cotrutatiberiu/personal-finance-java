package com.trier.trier_report.service.impl;

import com.trier.trier_report.dao.AccountRepository;
import com.trier.trier_report.dto.AccountArchiveRequest;
import com.trier.trier_report.dto.AccountCreateRequest;
import com.trier.trier_report.dto.AccountResponse;
import com.trier.trier_report.dto.AccountUpdateRequest;
import com.trier.trier_report.entity.Account;
import com.trier.trier_report.exception.DuplicateResourceException;
import com.trier.trier_report.mapper.AccountMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    void createAccount_Should_BeCalled() {
        Long userId = 1L;
        Long accountTypeId = 1L;
        Long currencyId = 1L;
        String name = "BANK";
        AccountCreateRequest request = new AccountCreateRequest(userId, accountTypeId, currencyId, name);
        accountService.create(request);

        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void updateAccount_Should_UpdateAllFields_When_DataIsNew() {
        Long accountId = 1L;
        Long existingCurrencyId = 1L;
        String existingName = "CARD";
        Long newCurrencyId = 1L;
        String newName = "BANK";

        Account account = new Account(accountId, 1L, 1L, existingName);

        AccountUpdateRequest request = new AccountUpdateRequest(accountId, newName, newCurrencyId);

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        AccountResponse response = accountService.update(request);

        assertEquals(newName, response.name());
        assertEquals(existingCurrencyId, account.getUserId());
        assertEquals(newCurrencyId, response.currencyId());
    }

    @Test
    void updateAccount_Should_ThrowException_When_DuplicateAccountName() {
        Long userId = 1L;
        Long accountId = 1L;
        Long existingCurrencyId = 1L;
        String existingName = "CARD";
        String newName = "BANK";
        Long newCurrencyId = 2L;

        Account account = new Account(accountId, userId, existingCurrencyId, existingName);

        AccountUpdateRequest request = new AccountUpdateRequest(accountId, newName, newCurrencyId);

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(accountRepository.existsByUserIdAndNameIgnoreCase(account.getUserId(), request.name())).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> {
            accountService.update(request);
        });

        verify(accountRepository, never()).save(any());
    }

    @Test
    void archive_Should_ReturnAccountResponse() {
        Long accountId = 1L;
        Long accountTypeId = 1L;
        Long currencyId = 1L;
        String name = "CARD";
        Account account = new Account(accountId, accountTypeId, currencyId, name);
        boolean archived = true;
        AccountArchiveRequest request = new AccountArchiveRequest(archived);
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        AccountResponse response = accountService.archive(accountId, request);

        assertTrue(response.archived());
    }

    @Test
    void archive_Should_ThrowException_When_AccountNotFound() {
        Long accountId = 1L;
        Long accountTypeId = 1L;
        Long currencyId = 1L;
        String name = "CARD";
        Account account = new Account(accountId, accountTypeId, currencyId, name);

        boolean archived = true;
        AccountArchiveRequest request = new AccountArchiveRequest(archived);
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            accountService.archive(accountId, request);
        });
    }
}