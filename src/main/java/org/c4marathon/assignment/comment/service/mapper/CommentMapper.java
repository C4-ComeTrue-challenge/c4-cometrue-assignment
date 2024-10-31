package org.c4marathon.assignment.comment.service.mapper;

import static org.c4marathon.assignment.board.domain.WriterType.*;

import org.c4marathon.assignment.board.domain.Boards;
import org.c4marathon.assignment.comment.domain.Comment;
import org.c4marathon.assignment.comment.dto.CommentCreateRequest;
import org.c4marathon.assignment.user.domain.Users;

public class CommentMapper {

	public static Comment toComment(CommentCreateRequest request, Users users, String path, Boards board,
		Comment parent) {
		return Comment.builder()
			.content(request.content())
			.path(path)
			.board(board)
			.parent(parent)
			.users(users)
			.writerType(USER)
			.build();
	}

	public static Comment toComment(CommentCreateRequest request, String path, Boards board, Comment parent) {
		return Comment.builder()
			.content(request.content())
			.path(path)
			.board(board)
			.parent(parent)
			.writerName(request.writerName())
			.password(request.password())
			.writerType(GUEST)
			.build();
	}
}
