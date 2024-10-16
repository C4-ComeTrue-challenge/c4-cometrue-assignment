package org.c4marathon.assignment.post.service.dto;

import lombok.Builder;

@Builder
public record PostCreateServiceByUserRequest(
        String title,
        String content
) {

}
