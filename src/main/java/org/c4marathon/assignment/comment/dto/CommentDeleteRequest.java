package org.c4marathon.assignment.comment.dto;

import jakarta.validation.constraints.Size;

public record CommentDeleteRequest(
	@Size(max = 20)
	String password
) {
}
