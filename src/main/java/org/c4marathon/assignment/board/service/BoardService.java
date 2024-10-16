package org.c4marathon.assignment.board.service;

import org.c4marathon.assignment.board.domain.Boards;
import org.c4marathon.assignment.board.domain.repository.BoardRepository;
import org.c4marathon.assignment.board.dto.BoardCreateRequest;
import org.c4marathon.assignment.board.dto.BoardGetAllResponse;
import org.c4marathon.assignment.board.dto.BoardGetOneResponse;
import org.c4marathon.assignment.user.domain.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {

	private final BoardRepository boardRepository;

	@Transactional
	public long createBoardAsUser(BoardCreateRequest request, Users users) {
		Boards boards = toBoard(request, users);
		return boardRepository.save(boards).getId();
	}

	@Transactional
	public long createBoardAsGuest(BoardCreateRequest request) {
		Boards boards = toBoard(request);
		return boardRepository.save(boards).getId();
	}

	@Transactional(readOnly = true)
	public Page<BoardGetAllResponse> getAllBoards(Pageable pageable) {
		return boardRepository.getAll(pageable);
	}

	@Transactional(readOnly = true)
	public BoardGetOneResponse getOneBoard(Long id) {
		Boards boards = boardRepository.getById(id);
		return toDto(boards);
	}

	private Boards toBoard(BoardCreateRequest request, Users users) {
		return Boards.builder().title(request.title()).content(request.content()).users(users).build();
	}

	private Boards toBoard(BoardCreateRequest request) {
		return Boards.builder()
			.title(request.title())
			.content(request.content())
			.writerName(request.writerName())
			.password(request.password())
			.build();
	}

	private BoardGetOneResponse toDto(Boards boards) {
		return new BoardGetOneResponse(boards.getId(), boards.getContent(), boards.getTitle(), boards.getWriterName(),
			boards.getCreatedDate(), boards.getLastModifiedDate());
	}

}
