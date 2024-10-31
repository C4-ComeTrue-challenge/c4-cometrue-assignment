package org.c4marathon.assignment.board.dto;

import jakarta.validation.constraints.Size;

public record BoardDeleteRequest(
	@Size(max = 20)
	String password
) {
}
