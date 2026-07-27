package com.trier.trier_report.service.impl;

import com.trier.trier_report.dao.UserRepository;
import com.trier.trier_report.dto.LoginResult;
import com.trier.trier_report.dto.RefreshAccessTokenResponse;
import com.trier.trier_report.dto.UserRegisterRequest;
import com.trier.trier_report.dto.UserResponse;
import com.trier.trier_report.entity.User;
import com.trier.trier_report.mapper.UserMapper;
import com.trier.trier_report.service.AuthService;
import com.trier.trier_report.service.UserRoleService;
import com.trier.trier_report.service.UserService;
import com.trier.trier_report.util.JwtUtil;
import com.trier.trier_report.util.StringUtil;
import io.jsonwebtoken.Claims;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final UserService userService;
    private final UserRoleService userRoleService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository, UserService userService, UserRoleService userRoleService, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.userService = userService;
        this.userRoleService = userRoleService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    @Transactional
    public UserResponse register(UserRegisterRequest request) {

        User user = UserMapper.toEntity(request);

        User savedUser = userService.create(user, request.password());

        return UserMapper.toUserResponse(savedUser);
    }

    @Override
    public LoginResult login(String email, String password) {
        String normalizedEmail = StringUtil.normalizeEmail(email.trim().toLowerCase(Locale.ROOT));
        User user = userRepository.findByEmail(normalizedEmail).orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(normalizedEmail, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtUtil.generateAccessToken(normalizedEmail, userRoleService.getRoles(user.getId()));
        String refreshToken = jwtUtil.generateRefreshToken(normalizedEmail);

        return new LoginResult(accessToken, refreshToken);
    }

    @Override
    public RefreshAccessTokenResponse refreshAccessToken(String refreshToken) {
        String newAccessToken = jwtUtil.refreshAccessToken(refreshToken, jwtUtil.getRolesFromRefreshToken(refreshToken));
        return new RefreshAccessTokenResponse(newAccessToken);
    }

    @Override
    public String isAuthenticated() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return auth.getName();
    }
}
