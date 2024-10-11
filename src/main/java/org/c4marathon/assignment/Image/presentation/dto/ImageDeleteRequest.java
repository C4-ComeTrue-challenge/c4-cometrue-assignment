package org.c4marathon.assignment.Image.presentation.dto;

import org.c4marathon.assignment.Image.service.dto.ImageDeleteServiceRequest;

public record ImageDeleteRequest(
        String imageUrl
) {

    public ImageDeleteServiceRequest toServiceDto() {
        return ImageDeleteServiceRequest.builder()
                .imageUrl(imageUrl)
                .build();
    }
}
