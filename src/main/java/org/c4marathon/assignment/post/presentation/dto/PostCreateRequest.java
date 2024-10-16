package org.c4marathon.assignment.post.presentation.dto;

import org.c4marathon.assignment.post.service.dto.PostCreateServiceRequest;

public record PostCreateRequest(
        String title,
        String content,
        String guestName,
        String guestPassword
) {
    public PostCreateServiceRequest toServiceDto() {
        return PostCreateServiceRequest.builder()
                .title(title)
                .content(content)
                .build();
    }
}
