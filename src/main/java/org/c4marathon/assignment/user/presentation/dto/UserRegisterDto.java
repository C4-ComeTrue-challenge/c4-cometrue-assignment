package org.c4marathon.assignment.user.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.c4marathon.assignment.user.service.dto.UserRegisterServiceDto;

public record UserRegisterDto(

        @NotBlank(message = "email을 입력해주세요.")
        String email,

        @NotBlank(message = "Password를 입력해주세요.")
        String password,

        @NotBlank(message = "닉네임을 입력해주세요.")
        String nickname
) {
    public UserRegisterServiceDto toServiceDto() {
        return UserRegisterServiceDto.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .build();
    }

}
