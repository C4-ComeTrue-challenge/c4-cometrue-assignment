package org.c4marathon.assignment.user.service.dto;

import lombok.Builder;

@Builder
public record UserLoginServiceDto(
        String email,
        String password
) {
}
