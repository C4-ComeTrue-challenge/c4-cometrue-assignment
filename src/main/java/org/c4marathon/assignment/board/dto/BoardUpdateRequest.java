package org.c4marathon.assignment.board.dto;

import jakarta.validation.constraints.Size;

public record BoardUpdateRequest(
	@Size(max = 100)
	String title,

	@Size(max = 65535)
	String content,

	@Size(max = 20)
	String password
) {
}
