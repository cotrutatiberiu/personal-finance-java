package com.trier.trier_report.service.impl;

import com.trier.trier_report.dao.AccountRepository;
import com.trier.trier_report.dto.AccountArchiveRequest;
import com.trier.trier_report.dto.AccountResponse;
import com.trier.trier_report.dto.AccountUpdateRequest;
import com.trier.trier_report.dto.AccountCreateRequest;
import com.trier.trier_report.entity.Account;
import com.trier.trier_report.exception.DuplicateResourceException;
import com.trier.trier_report.mapper.AccountMapper;
import com.trier.trier_report.service.AccountService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional(readOnly = true)
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
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
}
