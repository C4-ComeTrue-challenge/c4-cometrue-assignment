package org.c4marathon.assignment.user.service.mapper;

import org.c4marathon.assignment.user.domain.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public static User toUser(String email, String encodedPassword, String nickname){
        return User.builder()
                .email(email)
                .password(encodedPassword)
                .nickname(nickname)
                .build();
    }

}
