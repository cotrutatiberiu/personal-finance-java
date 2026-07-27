package com.trier.trier_report.dao;

import com.trier.trier_report.entity.UserRole;
import com.trier.trier_report.entity.id.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {
    List<UserRole> findAllByUserId(Long userId);
}
