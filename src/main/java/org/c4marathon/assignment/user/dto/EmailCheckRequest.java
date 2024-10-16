package org.c4marathon.assignment.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EmailCheckRequest(
        @Email
        @NotBlank
        @Size(max = 20)
        String email
) {
}
