package org.c4marathon.assignment.post.service.dto;

import lombok.Builder;

@Builder
public record PostDeleteServiceRequest(
        String guestName,
        String guestPassword
) {
}
