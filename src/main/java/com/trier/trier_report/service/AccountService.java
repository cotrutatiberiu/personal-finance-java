package com.trier.trier_report.service;

import com.trier.trier_report.dto.*;

public interface AccountService {
    void create(AccountCreateRequest payload);
    AccountResponse update(AccountUpdateRequest payload);
    AccountResponse archive(Long id, AccountArchiveRequest payload);
    AccountResponse findAccountByUserId(Long userId, Long accountId);
    PaginatedResponse<AccountResponse> findAccountsByUserId(Long userId, UserAccountsSearchRequest request);
}
