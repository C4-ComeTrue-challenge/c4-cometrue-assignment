package org.c4marathon.assignment.auth.dto;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {
}
