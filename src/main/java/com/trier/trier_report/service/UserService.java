package com.trier.trier_report.service;

import com.trier.trier_report.dto.AccountResponse;
import com.trier.trier_report.dto.PaginatedResponse;
import com.trier.trier_report.dto.UserAccountsSearchRequest;
import com.trier.trier_report.entity.User;

public interface UserService {
    User create(User user, String password);
}
