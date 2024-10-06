package org.c4marathon.assignment.board.dto;

import java.time.LocalDateTime;

public record BoardGetAllResponse(
        String title,
        String writerName,
        LocalDateTime createdDate,
        LocalDateTime lastModifiedDate
) {
}
