package com.trier.trier_report.dao;

import com.trier.trier_report.entity.UserRole;
import com.trier.trier_report.entity.id.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRolesRepository extends JpaRepository<UserRole, UserRoleId> {
}
