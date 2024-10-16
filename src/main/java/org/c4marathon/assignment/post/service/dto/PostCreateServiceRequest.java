package org.c4marathon.assignment.post.service.dto;

import lombok.Builder;

@Builder
public record PostCreateServiceRequest(
        String title,
        String content,
        String guestName,
        String guestPassword
) {

}
