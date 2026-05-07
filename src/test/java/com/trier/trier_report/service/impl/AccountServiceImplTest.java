package com.trier.trier_report.service.impl;

import com.trier.trier_report.dao.AccountRepository;
import com.trier.trier_report.dto.AccountArchiveRequest;
import com.trier.trier_report.dto.AccountCreateRequest;
import com.trier.trier_report.dto.AccountResponse;
import com.trier.trier_report.dto.AccountUpdateRequest;
import com.trier.trier_report.entity.Account;
import com.trier.trier_report.mapper.AccountMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    void updateAccount_Should_ReturnAccountResponse() {
        Long accountId = 1L;
        Long currencyId = 1L;
        String name = "CARD";
        Account account = new Account(accountId, 1L, 1L, name);

        AccountUpdateRequest request = new AccountUpdateRequest(accountId, name, currencyId);

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        AccountResponse response = accountService.update(request);

        assertEquals("CARD", response.name());
    }

    @Test
    void archive() {
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
}