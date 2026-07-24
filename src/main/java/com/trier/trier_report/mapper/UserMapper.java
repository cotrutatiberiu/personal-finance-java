package com.trier.trier_report.mapper;

import com.trier.trier_report.dto.UserRegisterRequest;
import com.trier.trier_report.dto.UserResponse;
import com.trier.trier_report.entity.User;

public class UserMapper {
    public static User toEntity(UserRegisterRequest payload) {
        return new User(payload.firstName(), payload.lastName(), payload.email());
    }

    public static UserResponse toUserResponse(User user) {
        return new UserResponse(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());
    }
}
