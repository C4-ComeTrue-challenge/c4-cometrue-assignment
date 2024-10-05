package org.c4marathon.assignment.board.presentation.dto;

import org.c4marathon.assignment.board.service.dto.BoardCreateServiceRequest;

public record BoardCreateRequest(
        String name
) {
    public BoardCreateServiceRequest toServiceDto() {
        return BoardCreateServiceRequest.builder()
                .name(name)
                .build();
    }
}
