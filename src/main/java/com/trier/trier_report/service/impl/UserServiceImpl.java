package com.trier.trier_report.service.impl;

import com.trier.trier_report.dao.RoleRepository;
import com.trier.trier_report.dao.UserRepository;
import com.trier.trier_report.dao.UserRoleRepository;
import com.trier.trier_report.entity.Role;
import com.trier.trier_report.entity.User;
import com.trier.trier_report.entity.UserRole;
import com.trier.trier_report.exception.EmailUsedException;
import com.trier.trier_report.service.UserService;
import com.trier.trier_report.util.StringUtil;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserRoleRepository userRoleRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User create(User user, String password) {
        String normalizedEmail = StringUtil.normalizeEmail(user.getEmail());
        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new EmailUsedException("Email already used");
        }

        String encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);
        user.setEmail(normalizedEmail);

        User savedUser = userRepository.save(user);

        Role role = roleRepository.findByName("USER").orElseThrow(() -> new EntityNotFoundException("Role not found"));

        userRoleRepository.save(new UserRole(savedUser, role));

        return savedUser;
    }
}
