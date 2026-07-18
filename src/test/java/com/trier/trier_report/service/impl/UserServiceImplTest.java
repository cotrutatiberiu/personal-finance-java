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


}