package org.c4marathon.assignment.user.service.dto;

import lombok.Builder;

@Builder
public record UserLoginServiceRequest(
        String email,
        String password
) {
}
