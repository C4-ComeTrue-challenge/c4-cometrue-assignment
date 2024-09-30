package org.c4marathon.assignment.board.service.dto;

import lombok.Builder;

@Builder
public record BoardUpdateServiceRequest(
        Long boardId,
        String name
) {
}
