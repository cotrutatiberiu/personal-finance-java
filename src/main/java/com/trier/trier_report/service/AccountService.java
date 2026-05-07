package com.trier.trier_report.service;

import com.trier.trier_report.dto.AccountArchiveRequest;
import com.trier.trier_report.dto.AccountResponse;
import com.trier.trier_report.dto.AccountUpdateRequest;
import com.trier.trier_report.dto.AccountCreateRequest;

public interface AccountService {
    void create(AccountCreateRequest payload);
    AccountResponse update(AccountUpdateRequest payload);
    AccountResponse archive(Long id, AccountArchiveRequest payload);
}
