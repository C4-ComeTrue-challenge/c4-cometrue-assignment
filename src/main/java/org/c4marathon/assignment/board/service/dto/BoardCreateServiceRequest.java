package org.c4marathon.assignment.board.service.dto;

import lombok.Builder;

@Builder
public record BoardCreateServiceRequest(
        String name
) {
}
