package com.trier.trier_report.service.impl;

import com.trier.trier_report.dao.AccountRepository;
import com.trier.trier_report.dao.UserRepository;
import com.trier.trier_report.dto.AccountResponse;
import com.trier.trier_report.dto.PaginatedResponse;
import com.trier.trier_report.dto.UserAccountsSearchRequest;
import com.trier.trier_report.entity.Account;
import com.trier.trier_report.enums.AccountSortField;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private AccountRepository accountRepository;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void findAccountsByUserId_Should_ReturnPaginatedAccounts_When_UserExists() {
        Long userId = 1L;
        UserAccountsSearchRequest request = new UserAccountsSearchRequest(1, 10, AccountSortField.NAME);

        when(userRepository.existsById(userId)).thenReturn(true);

        Account account = new Account(1L, 1L, 1L, "BANK"); // Assume basic setters exist
        Page<Account> accountsPage = new PageImpl<>(List.of(account));
        when(accountRepository.findAllByUserId(eq(userId), any(Pageable.class))).thenReturn(accountsPage);

        // Act
        PaginatedResponse<AccountResponse> response = userService.findAccountsByUserId(userId, request);

        // Assert
        assertNotNull(response);
        verify(userRepository, times(1)).existsById(userId);
        verify(accountRepository, times(1)).findAllByUserId(eq(userId), any(Pageable.class));
    }

    @Test
    void findAccountsByUserId_Should_ReturnError_When_UserDoesNotExist() {
        // Arrange
        Long userId = 1L;
        UserAccountsSearchRequest request = new UserAccountsSearchRequest(1, 10, AccountSortField.NAME);

        when(userRepository.existsById(userId)).thenReturn(false);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            userService.findAccountsByUserId(userId, request);
        });

        // Verify
        verify(userRepository, times(1)).existsById(userId);
        verifyNoInteractions(accountRepository);
    }


    @Test
    void findAccountByUserId_Should_ReturnAccount_When_UserExists() {
        Long userId = 1L;
        Long accountId = 1L;
        Account account = new Account(userId, 1L, 1L, "BANK");

        when(accountRepository.findByIdAndUserId(userId, accountId))
                .thenReturn(Optional.of(account));

        AccountResponse response = userService.findAccountByUserId(userId, accountId);

        assertNotNull(response);
        assertEquals("BANK", response.name());

        verify(accountRepository, times(1)).findByIdAndUserId(userId, accountId);
    }

    @Test
    void findAccountByUserId_Should_ReturnError_When_UserDoesNotExists() {
        Long userId = 1L;
        Long accountId = 1L;

        when(accountRepository.findByIdAndUserId(userId, accountId))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            userService.findAccountByUserId(userId, accountId);
        });

        verify(accountRepository, times(1)).findByIdAndUserId(userId, accountId);
    }
}