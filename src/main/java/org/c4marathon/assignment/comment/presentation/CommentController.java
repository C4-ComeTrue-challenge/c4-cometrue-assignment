package org.c4marathon.assignment.comment.presentation;

import org.c4marathon.assignment.board.dto.BoardDeleteRequest;
import org.c4marathon.assignment.board.dto.BoardGetAllResponse;
import org.c4marathon.assignment.board.dto.PageInfo;
import org.c4marathon.assignment.comment.dto.CommentCreateRequest;
import org.c4marathon.assignment.comment.dto.CommentGetAllResponse;
import org.c4marathon.assignment.comment.service.CommentService;
import org.c4marathon.assignment.global.annotation.LoginUser;
import org.c4marathon.assignment.user.domain.Users;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@RequestMapping("/api/{boardId}/comment")
@RequiredArgsConstructor
@RestController
public class CommentController {

	private final CommentService commentService;

	@PostMapping
	public ResponseEntity<Void> createComment(@PathVariable Long boardId,
		@RequestParam(required = false) Long parentCommentId, @Valid @RequestBody CommentCreateRequest request,
		@LoginUser Users loginUser) {
		if (loginUser == null) {
			if (request.writerName() == null || request.password() == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			}
			commentService.createComment(boardId, parentCommentId, request, null);
		} else {
			commentService.createComment(boardId, parentCommentId, request, loginUser);
		}

		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteComment(
		@Valid @RequestBody BoardDeleteRequest request,
		@PathVariable Long id,
		@LoginUser Users loginUser
	) {
		if (loginUser == null) {
			if (request.password() == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			}
			commentService.deleteComment(id, null, request.password());
		} else {
			commentService.deleteComment(id, loginUser.getNickname(), null);
		}
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@GetMapping
	public ResponseEntity<PageInfo<BoardGetAllResponse>> getAllComments(
		@PathVariable Long boardId,
		@RequestParam(required = false) String pageToken,
		@RequestParam @Positive @Max(100) int count
	) {
		PageInfo<CommentGetAllResponse> response = commentService.getAllComments(boardId, pageToken, count);
		return ResponseEntity.ok(null);
	}
}
