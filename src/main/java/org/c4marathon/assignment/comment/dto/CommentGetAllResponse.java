package org.c4marathon.assignment.comment.dto;

import java.time.LocalDateTime;
import java.util.List;

public record CommentGetAllResponse(
	Long id,
	String content,
	String writerName,
	LocalDateTime createdDate,
	LocalDateTime lastModifiedDate,
	List<CommentGetAllResponse> childComment
) {
}
