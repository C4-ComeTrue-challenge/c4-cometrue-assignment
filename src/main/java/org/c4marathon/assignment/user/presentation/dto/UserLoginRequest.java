package org.c4marathon.assignment.user.presentation.dto;

import org.c4marathon.assignment.user.service.dto.UserLoginServiceRequest;

public record UserLoginRequest(
        String email,
        String password

) {
    public UserLoginServiceRequest toServiceDto() {
        return UserLoginServiceRequest.builder()
                .email(email)
                .password(password)
                .build();
    }
}
