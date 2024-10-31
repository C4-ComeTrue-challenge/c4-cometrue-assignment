package org.c4marathon.assignment.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentCreateRequest(
	@NotBlank
	@Size(max = 1000)
	String content,

	@Size(max = 20)
	String writerName,

	@Size(max = 20)
	String password

) {
}
