package org.c4marathon.assignment.comment.domain.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.c4marathon.assignment.comment.domain.Comment;
import org.c4marathon.assignment.comment.dto.CommentGetAllResponse;

public interface CommentCustomRepository {
	Optional<Comment> findNotDeletedById(Long id);

	List<CommentGetAllResponse> findComments(long boardId, int limit);

	List<CommentGetAllResponse> findCommentsWithPageToken(long boardId, LocalDateTime createdDate, String path,
		int limit);

}
