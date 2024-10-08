package org.c4marathon.assignment.domin.user.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.Getter;
import org.c4marathon.assignment.domin.user.annotation.ExistEmail;
import org.c4marathon.assignment.domin.user.entity.Role;

public class UserRequestDTO {

    @Data
    public static class signupRequestDTO {
        @NotBlank
        @Email
        @ExistEmail
        private String email;

        @NotBlank
        private String password;

        @NotBlank
        private String name;

        @NotBlank
        private Role role;

        @NotBlank
        private String account;

        @NotBlank
        private String bank;
    }

    @Data
    public static class loginRequestDTO {
        @NotBlank
        @Email
        private String email;

        @NotBlank
        private String password;
    }

    @Data
    public static class CacheChargeRequestDTO {
        @Positive
        private Integer amount;
    }
}
