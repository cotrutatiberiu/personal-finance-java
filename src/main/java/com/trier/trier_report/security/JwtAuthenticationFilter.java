package com.trier.trier_report.security;

import com.trier.trier_report.util.JwtUtil;
import com.trier.trier_report.service.impl.CustomUserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final CustomUserDetailsServiceImpl customUserDetailsServiceImpl;
    private final HandlerExceptionResolver resolver;
    private final JwtUtil jwtUtil;

    private static final List<String> EXCLUDED_PATHS = List.of(
            "/api/auth/login",
            "/api/auth/register",
            "/api/auth/refresh-token",
            "/api/auth/csrf"
    );

    public JwtAuthenticationFilter(CustomUserDetailsServiceImpl customUserDetailsServiceImpl, @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver, JwtUtil jwtUtil) {
        this.customUserDetailsServiceImpl = customUserDetailsServiceImpl;
        this.resolver = resolver;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();

        // Always allow preflight
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        if (EXCLUDED_PATHS.contains(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = jwtUtil.getAccessTokenFromRequest(request);
        System.out.println(accessToken);

        // No token? just continue
        if (accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!jwtUtil.validateAccessToken(accessToken)) {
            filterChain.doFilter(request, response);
            return;
        }

        String email = jwtUtil.getEmailFromAccessToken(accessToken);
        UserDetails userDetails = customUserDetailsServiceImpl.loadUserByUsername(email);

        var auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(auth);

        filterChain.doFilter(request, response);
    }
}
