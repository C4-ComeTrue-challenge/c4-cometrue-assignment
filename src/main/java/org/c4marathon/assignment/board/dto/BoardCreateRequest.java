package org.c4marathon.assignment.board.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BoardCreateRequest(
	@NotBlank
	@Size(max = 100)
	String title,

	@NotBlank
	@Size(max = 65535)
	String content,

	@Size(max = 20)
	String writerName,

	@Size(max = 20)
	String password
) {
}
