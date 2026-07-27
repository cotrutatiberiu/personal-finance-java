package com.trier.trier_report.service;

import com.trier.trier_report.dto.LoginResult;
import com.trier.trier_report.dto.RefreshAccessTokenResponse;
import com.trier.trier_report.dto.UserRegisterRequest;
import com.trier.trier_report.dto.UserResponse;

public interface AuthService {
    UserResponse register(UserRegisterRequest request);

    LoginResult login(String email, String password);

    RefreshAccessTokenResponse refreshAccessToken(String refreshToken);

    String isAuthenticated();
}
