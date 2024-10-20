package org.c4marathon.assignment.post.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import org.c4marathon.assignment.post.service.dto.PostCreateServiceRequest;

public record PostCreateRequest(
        @NotBlank
        String title,

        @NotBlank
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
