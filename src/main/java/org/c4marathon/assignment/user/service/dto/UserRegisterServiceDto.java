package org.c4marathon.assignment.user.service.dto;

import lombok.Builder;

@Builder
public record UserRegisterServiceDto(
        String email,
        String password,
        String nickname
) {

}
