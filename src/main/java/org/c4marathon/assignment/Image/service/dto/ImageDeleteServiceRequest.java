package org.c4marathon.assignment.Image.service.dto;

import lombok.Builder;

@Builder
public record ImageDeleteServiceRequest(
        String imageUrl
) {
}
