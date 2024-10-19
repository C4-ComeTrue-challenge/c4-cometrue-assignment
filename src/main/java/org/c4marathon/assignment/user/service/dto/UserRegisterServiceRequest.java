package org.c4marathon.assignment.user.service.dto;

import lombok.Builder;

@Builder
public record UserRegisterServiceRequest(
        String email,
        String password,
        String nickname
) {

}
