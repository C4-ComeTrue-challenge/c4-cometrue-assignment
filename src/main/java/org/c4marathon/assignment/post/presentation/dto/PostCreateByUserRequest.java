package org.c4marathon.assignment.post.presentation.dto;

import org.c4marathon.assignment.post.service.dto.PostCreateServiceByUserRequest;

public record PostCreateByUserRequest(
        String title,
        String content
) {
    public PostCreateServiceByUserRequest toServiceDto() {
        return PostCreateServiceByUserRequest.builder()
                .title(title)
                .content(content)
                .build();
    }
}
