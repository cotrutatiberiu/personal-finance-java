package com.trier.trier_report.util;

import java.util.Locale;

public final class StringUtil {
    public static String normalizeEmail(String email) {
        if (email == null) {
            return null;
        }
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
