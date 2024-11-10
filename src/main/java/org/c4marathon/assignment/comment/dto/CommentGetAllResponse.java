package org.c4marathon.assignment.comment.dto;

import java.time.LocalDateTime;

public record CommentGetAllResponse(
	Long commentId,
	String content,
	String writerName,
	LocalDateTime createdDate,
	LocalDateTime lastModifiedDate,
	Long parentId,
	String path
) {
}
