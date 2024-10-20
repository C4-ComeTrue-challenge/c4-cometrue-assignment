package org.c4marathon.assignment.post.presentation.dto;

import org.c4marathon.assignment.post.service.dto.PostDeleteServiceRequest;

public record PostDeleteRequest(
        String guestName,
        String guestPassword
) {
    public PostDeleteServiceRequest toServiceDto() {
        return PostDeleteServiceRequest.builder()
                .guestName(guestName)
                .guestPassword(guestPassword)
                .build();
    }
}
