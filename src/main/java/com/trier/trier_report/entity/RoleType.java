package com.trier.trier_report.entity;

public enum RoleType {
    USER(Constants.USER),
    MODERATOR(Constants.MODERATOR),
    ADMIN(Constants.ADMIN);

    private final String value;

    RoleType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static class Constants {
        public static final String USER = "USER";
        public static final String MODERATOR = "MODERATOR";
        public static final String ADMIN = "ADMIN";
    }
}
