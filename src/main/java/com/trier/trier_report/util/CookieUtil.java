package com.trier.trier_report.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Objects;

public final class CookieUtil {
    public static String getValueFromCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        String cookieValue = null;

        for (Cookie cookie : cookies) {
            if (Objects.equals(cookieName, cookie.getName())) {
                cookieValue = cookie.getValue();
                break;
            }
        }

        return cookieValue;
    }
}
