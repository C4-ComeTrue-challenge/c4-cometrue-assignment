package org.c4marathon.assignment.post.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import org.c4marathon.assignment.post.service.dto.PostUpdateServiceRequest;

public record PostUpdateRequest(
        @NotBlank
        String title,

        @NotBlank
        String content,
        String guestName,
        String guestPassword

) {
    public PostUpdateServiceRequest toServiceDto() {
        return PostUpdateServiceRequest.builder()
                .title(title)
                .content(content)
                .guestName(guestName)
                .guestPassword(guestPassword)
                .build();
    }
}
