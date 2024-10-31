package org.c4marathon.assignment.comment.domain.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.c4marathon.assignment.board.dto.PageInfo;
import org.c4marathon.assignment.comment.domain.Comment;
import org.c4marathon.assignment.comment.dto.CommentGetAllResponse;
import org.c4marathon.assignment.comment.exception.NotFoundCommentException;
import org.c4marathon.assignment.global.utils.PageTokenUtils;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CommentRepository {

	private final CommentJpaRepository commentJpaRepository;

	public void save(Comment comment) {
		commentJpaRepository.save(comment);
	}

	public Comment getById(Long id) {
		return commentJpaRepository.findNotDeletedById(id)
			.orElseThrow(() -> new NotFoundCommentException());
	}

	public PageInfo<CommentGetAllResponse> findCommentsWithoutPageToken(long boardId, int size) {
		List<CommentGetAllResponse> data = commentJpaRepository.findComments(boardId, size + 1);

		return PageInfo.of(data, size, CommentGetAllResponse::createdDate, CommentGetAllResponse::id);
	}

	public PageInfo<CommentGetAllResponse> findCommentsWithPageToken(long boardId, String pageToken, int size) {
		var pageData = PageTokenUtils.decodePageToken(pageToken, LocalDateTime.class, Long.class);
		var createdDate = pageData.getLeft();
		var commentId = pageData.getRight();
		var path = commentJpaRepository.findNotDeletedById(commentId).get().getPath();

		List<CommentGetAllResponse> data = commentJpaRepository.findCommentsWithPageToken(boardId, createdDate, path,
			size + 1);

		return PageInfo.of(data, size, CommentGetAllResponse::createdDate, CommentGetAllResponse::id);
	}
}
