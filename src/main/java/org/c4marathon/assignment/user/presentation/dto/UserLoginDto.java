package org.c4marathon.assignment.user.presentation.dto;

import org.c4marathon.assignment.user.service.dto.UserLoginServiceDto;

public record UserLoginDto(
        String email,
        String password

) {
    public UserLoginServiceDto toServiceDto() {
        return UserLoginServiceDto.builder()
                .email(email)
                .password(password)
                .build();
    }
}
