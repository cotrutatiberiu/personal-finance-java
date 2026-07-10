package com.trier.trier_report.service.impl;

import com.trier.trier_report.dao.AccountRepository;
import com.trier.trier_report.dao.UserRepository;
import com.trier.trier_report.dto.AccountResponse;
import com.trier.trier_report.dto.PaginatedResponse;
import com.trier.trier_report.dto.UserAccountsSearchRequest;
import com.trier.trier_report.entity.Account;
import com.trier.trier_report.mapper.AccountMapper;
import com.trier.trier_report.mapper.PaginationMapper;
import com.trier.trier_report.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    @Override
    public PaginatedResponse<AccountResponse> findAccountsByUserId(Long userId, UserAccountsSearchRequest request) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("User not found: " + userId);
        }

        int pageNum = Math.max(0, request.pageNumber() - 1);
        int pageSize = request.pageSize();

        Sort sort = Sort.by(request.sortBy().getField()).ascending();

        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);

        Page<Account> accounts = accountRepository.findAllByUserId(userId, pageable);

        return PaginationMapper.toDto(accounts, AccountMapper::toDto);
    }

    @Override
    public AccountResponse findAccountByUserId(Long userId, Long accountId) {
        Account account = accountRepository.findByIdAndUserId(userId, accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found: " + accountId));

        return AccountMapper.toDto(account);
    }
}
