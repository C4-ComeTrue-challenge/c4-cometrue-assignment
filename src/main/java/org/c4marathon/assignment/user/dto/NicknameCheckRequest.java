package org.c4marathon.assignment.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NicknameCheckRequest(
        @Size(max = 20)
        @NotBlank
        String nickname
) {
}