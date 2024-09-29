package org.c4marathon.assignment.user.presentation.dto;

public record UserRegisterResponse(
        Long userId,
        String email,
        String nickname

) {
}
