package org.c4marathon.assignment.board.presentation.dto;

import org.c4marathon.assignment.board.service.dto.BoardUpdateServiceRequest;

public record BoardUpdateRequest(
        Long boardId,
        String name
) {
    public BoardUpdateServiceRequest toServiceDto() {
        return BoardUpdateServiceRequest.builder()
                .boardId(boardId)
                .name(name)
                .build();
    }

}
