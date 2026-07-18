package com.trier.trier_report.service.impl;

import com.trier.trier_report.dao.AccountRepository;
import com.trier.trier_report.dao.UserRepository;
import com.trier.trier_report.dto.*;
import com.trier.trier_report.entity.Account;
import com.trier.trier_report.exception.DuplicateResourceException;
import com.trier.trier_report.mapper.AccountMapper;
import com.trier.trier_report.mapper.PaginationMapper;
import com.trier.trier_report.service.AccountService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional(readOnly = true)
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void create(AccountCreateRequest payload) {
        Account account = AccountMapper.toEntity(payload);
        accountRepository.save(account);
    }

    @Override
    @Transactional
    public AccountResponse update(AccountUpdateRequest payload) {
        Account account = accountRepository.findById(payload.id()).orElseThrow(() -> new EntityNotFoundException("Account not found with ID " + payload.id()));

        if (!payload.name().equalsIgnoreCase(account.getName())) {
            if (accountRepository.existsByUserIdAndNameIgnoreCase(account.getUserId(), payload.name())) {
                throw new DuplicateResourceException("Duplicate account name");
            }

            account.setName(payload.name());
        }

        if (!payload.currencyId().equals(account.getCurrencyId()))
            account.setCurrencyId(payload.currencyId());

        return AccountMapper.toDto(account);
    }

    @Override
    @Transactional
    public AccountResponse archive(Long id, AccountArchiveRequest payload) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Account not found with ID " + id));

        if (account.isArchived() != payload.archived())
            account.setArchived(payload.archived());

        return AccountMapper.toDto(account);
    }

    @Override
    public PaginatedResponse<AccountResponse> findAccountsByUserId(Long userId, UserAccountsSearchRequest request) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("User not found: " + userId);
        }

        int pageNum = Math.max(0, request.search().pageNumber() - 1);
        int pageSize = request.search().pageSize();

        Sort sort = Sort.by(request.search().sortBy()).ascending();

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
