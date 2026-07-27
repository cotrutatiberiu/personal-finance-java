package com.trier.trier_report.util;

import com.trier.trier_report.exception.RefreshTokenExpiredException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
public final class JwtUtil {

    @Value("${auth.access-secret-token}")
    private String ACCESS_SECRET;
    @Value("${auth.refresh-secret-token}")
    private String REFRESH_SECRET;

    private final static long accessTokenExpirationSeconds = 15 * 60;
    private final static long refreshTokenExpirationSeconds = 7 * 24 * 60 * 60;

    private Key getAccessKey() {
        return Keys.hmacShaKeyFor(ACCESS_SECRET.getBytes(StandardCharsets.UTF_8));
    }

    private Key getRefreshKey() {
        return Keys.hmacShaKeyFor(REFRESH_SECRET.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(String email, List<String> roles) {
        System.out.println(roles);
        return Jwts.builder()
                .setSubject(email)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpirationSeconds * 1000L))
                .signWith(getAccessKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpirationSeconds * 1000L))
                .signWith(getRefreshKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateAccessToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getAccessKey()).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public boolean validateRefreshToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getRefreshKey()).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public String getEmailFromAccessToken(String token) {
        return Jwts.parserBuilder().setSigningKey(getAccessKey()).build().parseClaimsJws(token).getBody().getSubject();
    }

    public String getEmailFromRefreshToken(String token) {
        return Jwts.parserBuilder().setSigningKey(getRefreshKey()).build().parseClaimsJws(token).getBody().getSubject();
    }

    public long getDefaultRefreshTokenExpirationSeconds() {
        return refreshTokenExpirationSeconds;
    }

    public String refreshAccessToken(String cookieRefreshToken, List<String> roles) {
        if (cookieRefreshToken == null || cookieRefreshToken.isBlank()) {
            throw new RefreshTokenExpiredException("Missing refresh token");
        }

        if (validateRefreshToken(cookieRefreshToken)) {
            return generateAccessToken(getEmailFromRefreshToken(cookieRefreshToken), roles);
        }
        throw new RefreshTokenExpiredException("Validation failed, please login again.");
    }

    public String getAccessTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public List<String> getRolesFromAccessToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getAccessKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("roles", List.class);
    }

    public List<String> getRolesFromRefreshToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getRefreshKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("roles", List.class);
    }
}
