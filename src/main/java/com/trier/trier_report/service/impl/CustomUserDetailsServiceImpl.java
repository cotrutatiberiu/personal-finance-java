package com.trier.trier_report.service.impl;

import com.trier.trier_report.dao.UserRepository;
import com.trier.trier_report.dao.UserRoleRepository;
import com.trier.trier_report.entity.CustomUserDetails;
import com.trier.trier_report.entity.User;
import com.trier.trier_report.entity.UserRole;
import com.trier.trier_report.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    @Autowired
    public CustomUserDetailsServiceImpl(UserRepository userRepository, UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String normalizedEmail = StringUtil.normalizeEmail(username);
        User user = userRepository.findByEmail(normalizedEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

        List<UserRole> userRoleList = userRoleRepository.findAllByUserId(user.getId());

        return new CustomUserDetails(user, userRoleList);
    }
}