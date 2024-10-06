package org.c4marathon.assignment.board.dto;

import java.time.LocalDateTime;

public record BoardUpdateResponse(
        Long id,
        String content,
        String title,
        String writerName,
        LocalDateTime createdDate,
        LocalDateTime lastModifiedDate
) {
}
