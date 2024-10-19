package org.c4marathon.assignment.board.presentation;

import org.c4marathon.assignment.board.dto.BoardCreateRequest;
import org.c4marathon.assignment.board.dto.BoardDeleteRequest;
import org.c4marathon.assignment.board.dto.BoardGetAllResponse;
import org.c4marathon.assignment.board.dto.BoardGetOneResponse;
import org.c4marathon.assignment.board.dto.BoardUpdateRequest;
import org.c4marathon.assignment.board.dto.PageInfo;
import org.c4marathon.assignment.board.service.BoardService;
import org.c4marathon.assignment.global.annotation.LoginUser;
import org.c4marathon.assignment.user.domain.Users;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@RequestMapping("/api/board")
@RequiredArgsConstructor
@RestController
public class BoardController {

	private final BoardService boardService;

	@PostMapping
	public ResponseEntity<Void> createBoard(
		@Valid @RequestBody BoardCreateRequest request,
		@LoginUser Users loginUser
	) {
		if (loginUser == null) {
			if (request.writerName() == null || request.password() == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			}
			boardService.createBoardAsGuest(request);
		} else {
			boardService.createBoardAsUser(request, loginUser);
		}

		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@GetMapping("/all")
	public ResponseEntity<PageInfo<BoardGetAllResponse>> getAllBoards(
		@RequestParam(required = false) String pageToken,
		@RequestParam @Positive @Max(100) int count
	) {
		PageInfo<BoardGetAllResponse> response = boardService.getAllBoards(pageToken, count);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/{id}")
	public ResponseEntity<BoardGetOneResponse> getOneBoard(
		@PathVariable Long id
	) {
		BoardGetOneResponse response = boardService.getOneBoard(id);
		return ResponseEntity.ok(response);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Void> updateBoard(
		@Valid @RequestBody BoardUpdateRequest request,
		@PathVariable Long id,
		@LoginUser Users loginUser
	) {
		if (loginUser == null) {
			if (request.password() == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			}
			boardService.updateBoardAsGuest(id, request);
		} else {
			boardService.updateBoardAsUser(id, request, loginUser.getNickname());
		}

		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteBoard(
		@Valid @RequestBody BoardDeleteRequest request,
		@PathVariable Long id,
		@LoginUser Users loginUser
	) {
		if (loginUser == null) {
			if (request.password() == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			}
			boardService.deleteBoardAsGuest(id, request);
		} else {
			boardService.deleteBoardAsUser(id, loginUser.getNickname());
		}

		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
}
