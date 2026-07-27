package com.trier.trier_report.mapper;

import com.trier.trier_report.dto.UserRegisterRequest;
import com.trier.trier_report.dto.UserResponse;
import com.trier.trier_report.entity.User;
import com.trier.trier_report.util.StringUtil;

import java.util.Locale;

public class UserMapper {
    public static User toEntity(UserRegisterRequest payload) {
        return new User(payload.firstName(), payload.lastName(), StringUtil.normalizeEmail(payload.email().trim().toLowerCase(Locale.ROOT)));
    }

    public static UserResponse toUserResponse(User user) {
        return new UserResponse(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());
    }
}
