package org.c4marathon.assignment.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignupRequest(
        @Email
        @NotBlank
        @Size(max = 20)
        String email,

        @Size(max = 20)
        @NotBlank
        String password,

        @Size(max = 20)
        @NotBlank
        String nickname
) {
}