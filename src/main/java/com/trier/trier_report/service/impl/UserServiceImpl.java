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
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
}
