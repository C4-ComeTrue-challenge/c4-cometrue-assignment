package org.c4marathon.assignment.board.presentation;

import org.c4marathon.assignment.board.dto.BoardCreateRequest;
import org.c4marathon.assignment.board.dto.BoardGetAllResponse;
import org.c4marathon.assignment.board.dto.BoardGetOneResponse;
import org.c4marathon.assignment.board.service.BoardService;
import org.c4marathon.assignment.global.config.SessionConfig;
import org.c4marathon.assignment.user.domain.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/api/board")
@RequiredArgsConstructor
@RestController
public class BoardController {

	private final BoardService boardService;
	private final SessionConfig sessionConfig;

	@PostMapping
	public ResponseEntity<Void> createBoard(
		@Valid @RequestBody BoardCreateRequest request,
		HttpServletRequest httpServletRequest
	) {
		Users loginUsers = sessionConfig.getSessionUser(httpServletRequest);

		if (loginUsers == null) {
			if (request.writerName() == null || request.password() == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			}
			boardService.createBoardAsGuest(request);
		} else {
			boardService.createBoardAsUser(request, loginUsers);
		}

		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@GetMapping("/all")
	public ResponseEntity<Page<BoardGetAllResponse>> getAllBoards(
		Pageable pageable
	) {
		Page<BoardGetAllResponse> response = boardService.getAllBoards(pageable);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/{id}")
	public ResponseEntity<BoardGetOneResponse> getOneBoard(
		@PathVariable Long id
	) {
		BoardGetOneResponse response = boardService.getOneBoard(id);
		return ResponseEntity.ok(response);
	}

}
