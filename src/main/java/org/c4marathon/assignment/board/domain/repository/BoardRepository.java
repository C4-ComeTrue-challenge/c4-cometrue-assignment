package org.c4marathon.assignment.board.domain.repository;

import org.c4marathon.assignment.board.domain.Board;
import org.c4marathon.assignment.board.dto.BoardGetAllResponse;
import org.c4marathon.assignment.board.exception.NotFoundBoardException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BoardRepository {

	private final BoardJpaRepository boardJpaRepository;

	public Page<BoardGetAllResponse> getAll(Pageable pageable) {
		return boardJpaRepository.findAllWithPaging(pageable);
	}

	public Board getById(Long id) {
		return boardJpaRepository.findById(id)
			.orElseThrow(() -> new NotFoundBoardException());
	}

	public Board save(Board board) {
		return boardJpaRepository.save(board);
	}
}
