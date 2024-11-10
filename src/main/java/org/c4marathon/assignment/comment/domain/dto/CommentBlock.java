package org.c4marathon.assignment.comment.domain.dto;

import java.time.LocalDateTime;

public record CommentBlock(
	Long commentId,
	String content,
	String writerName,
	LocalDateTime createdDate,
	LocalDateTime lastModifiedDate,
	Long parentId,
	String path
) {
}
