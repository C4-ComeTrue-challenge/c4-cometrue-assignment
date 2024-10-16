package org.c4marathon.assignment.board.service;

import org.c4marathon.assignment.board.domain.Board;
import org.c4marathon.assignment.board.domain.repository.BoardRepository;
import org.c4marathon.assignment.board.dto.BoardCreateRequest;
import org.c4marathon.assignment.board.dto.BoardGetAllResponse;
import org.c4marathon.assignment.board.dto.BoardGetOneResponse;
import org.c4marathon.assignment.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {

	private final BoardRepository boardRepository;
	private final PasswordEncoder encoder;

	@Transactional
	public long createBoardAsUser(BoardCreateRequest request, User user) {
		Board board = toBoard(request, user);
		return boardRepository.save(board).getId();
	}

	@Transactional
	public long createBoardAsGuest(BoardCreateRequest request) {
		Board board = toBoard(request);
		return boardRepository.save(board).getId();
	}

	@Transactional(readOnly = true)
	public Page<BoardGetAllResponse> getAllBoards(Pageable pageable) {
		return boardRepository.getAll(pageable);
	}

	@Transactional(readOnly = true)
	public BoardGetOneResponse getOneBoard(Long id) {
		Board board = boardRepository.getById(id);
		return toDto(board);
	}

	private Board toBoard(BoardCreateRequest request, User user) {
		return Board.builder()
			.title(request.title())
			.content(request.content())
			.user(user)
			.build();
	}

	private Board toBoard(BoardCreateRequest request) {
		return Board.builder()
			.title(request.title())
			.content(request.content())
			.writerName(request.writerName())
			.password(encoder.encode(request.password()))
			.build();
	}

	private BoardGetOneResponse toDto(Board board) {
		return new BoardGetOneResponse(
			board.getId(),
			board.getContent(),
			board.getTitle(),
			board.getWriterName(),
			board.getCreatedDate(),
			board.getLastModifiedDate()
		);
	}

}
