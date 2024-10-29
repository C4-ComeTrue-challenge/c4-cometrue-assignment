package org.c4marathon.assignment.board.dto;

import java.time.LocalDateTime;

public record BoardGetAllResponse(
	Long id,
	String title,
	String content,
	String writerName,
	LocalDateTime createdDate,
	LocalDateTime lastModifiedDate
) {
}
