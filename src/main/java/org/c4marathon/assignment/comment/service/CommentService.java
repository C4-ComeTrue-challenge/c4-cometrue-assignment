package org.c4marathon.assignment.comment.service;

import static org.c4marathon.assignment.board.domain.WriterType.*;
import static org.c4marathon.assignment.global.dto.DeletionReason.*;

import java.time.Clock;

import org.c4marathon.assignment.board.domain.Boards;
import org.c4marathon.assignment.board.domain.repository.BoardRepository;
import org.c4marathon.assignment.board.dto.PageInfo;
import org.c4marathon.assignment.comment.domain.Comment;
import org.c4marathon.assignment.comment.domain.repository.CommentRepository;
import org.c4marathon.assignment.comment.dto.CommentCreateRequest;
import org.c4marathon.assignment.comment.dto.CommentGetAllResponse;
import org.c4marathon.assignment.comment.service.mapper.CommentMapper;
import org.c4marathon.assignment.user.domain.Users;
import org.c4marathon.assignment.user.exception.NotWriterException;
import org.c4marathon.assignment.user.exception.WrongPasswordException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {

	private final BoardRepository boardRepository;
	private final CommentRepository commentRepository;
	private final Clock clock;

	@Transactional
	public Long createComment(Long boardId, Long parentCommentId, CommentCreateRequest request, Users user) {
		Boards board = boardRepository.getById(boardId);
		String path = makePath(parentCommentId);
		Comment parentComment = null;
		if (parentCommentId != null)
			parentComment = commentRepository.getById(parentCommentId);
		Comment comment = createCommentEntity(request, user, path, board, parentComment);
		commentRepository.save(comment);
		return comment.getId();
	}

	@Transactional
	public void deleteComment(Long id, String writerName, String password) {
		Comment comment = commentRepository.getById(id);
		validateUserOrGuest(comment, writerName, password);
		comment.deleteComment(DELETED_BY_MEMBER.getMessage(), clock);
	}

	@Transactional(readOnly = true)
	public PageInfo<CommentGetAllResponse> getAllComments(long boardId, String pageToken, int size) {
		if (pageToken == null) {
			return commentRepository.findCommentsWithoutPageToken(boardId, size);
		} else {
			return commentRepository.findCommentsWithPageToken(boardId, pageToken, size);
		}
	}

	private Comment createCommentEntity(CommentCreateRequest request, Users user, String path, Boards board,
		Comment parentComment) {
		if (user == null)
			return CommentMapper.toComment(request, path, board, parentComment);
		return CommentMapper.toComment(request, user, path, board, parentComment);
	}

	private String makePath(Long parentCommentId) {
		if (parentCommentId == null) {
			return "";
		}

		Comment parentComment = commentRepository.getById(parentCommentId);

		if (parentComment.getPath().isEmpty())
			return String.valueOf(parentComment.getId());

		return parentComment.getPath() + "/" + parentComment.getId();
	}

	private void validateUserOrGuest(Comment comment, String writerName, String password) {
		if (writerName == null) { // GUEST
			if (comment.getWriterType() == GUEST && !comment.getPassword().equals(password)) {
				throw new WrongPasswordException();
			}
		} else { // USER
			if (comment.getWriterType() == USER && !comment.getWriterName().equals(writerName)) {
				throw new NotWriterException();
			}
		}
	}

}
