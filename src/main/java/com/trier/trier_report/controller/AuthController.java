package com.trier.trier_report.controller;

import com.trier.trier_report.entity.CustomUserDetails;
import com.trier.trier_report.service.AuthService;
import com.trier.trier_report.util.JwtUtil;
import com.trier.trier_report.dto.*;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthService authService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register") //@Valid for jakarta request body validation
    public ResponseEntity<UserResponse> register(@Valid @RequestBody UserRegisterRequest request) {
        UserResponse response = authService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody UserLoginRequest request, HttpServletResponse response) {
        LoginResult loginResult = authService.login(request.email(), request.password());

        Cookie refreshTokenCookie = new Cookie("rt", loginResult.refreshToken());
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/api/auth");
        refreshTokenCookie.setMaxAge((int) (jwtUtil.getDefaultRefreshTokenExpirationSeconds()));

        response.addCookie(refreshTokenCookie);

        return ResponseEntity.ok(new LoginResponse(loginResult.accessToken()));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Object> resetAccessToken(@CookieValue(value = "rt", required = false) String cookieRefreshToken) {
        return ResponseEntity.ok(authService.refreshAccessToken(cookieRefreshToken));
    }

    @GetMapping("/authenticated")
    public ResponseEntity<String> isAuthenticated() {
        System.out.println();
        return ResponseEntity.ok(authService.isAuthenticated());
    }

    @GetMapping("/debug")
    public ResponseEntity<String> debugUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.getPrincipal() instanceof CustomUserDetails userDetails) {
            Long userId = userDetails.getId();
            String userEmail = userDetails.getUsername();

            System.out.println("DEBUG: Current Logged-in User ID is: " + userId);
            return ResponseEntity.ok("User ID is: " + userId + ", email: " + userEmail);
        }

        return ResponseEntity.status(401).body("No authenticated user found");
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        Cookie refreshCookie = new Cookie("rt", null);
        refreshCookie.setMaxAge(0);
        refreshCookie.setPath("/api/auth");

        response.addCookie(refreshCookie);
        return ResponseEntity.noContent().header(HttpHeaders.SET_COOKIE, refreshCookie.toString()).build();
    }
}
