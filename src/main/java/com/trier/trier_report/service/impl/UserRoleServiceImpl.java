package com.trier.trier_report.service.impl;

import com.trier.trier_report.dao.UserRoleRepository;
import com.trier.trier_report.entity.UserRole;
import com.trier.trier_report.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleServiceImpl implements UserRoleService {
    private final UserRoleRepository userRoleRepository;

    @Autowired
    public UserRoleServiceImpl(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    public List<String> getRoles(Long userId) {
        List<UserRole> userRoles = userRoleRepository.findAllByUserId(userId);
        return userRoles.stream().map(userRole -> userRole.getRole().getName().name()
        ).toList();
    }
}
