package com.trier.trier_report.dao;

import com.trier.trier_report.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Page<Account> findAllByUserId(Long userId, Pageable pageable);
    Optional<Account> findByIdAndUserId(Long userId, Long accountId);
    boolean existsByUserIdAndNameIgnoreCase(Long userId, String name);
}
