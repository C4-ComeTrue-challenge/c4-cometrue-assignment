package org.c4marathon.assignment.board.domain.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.c4marathon.assignment.board.domain.Boards;
import org.c4marathon.assignment.board.dto.BoardGetAllResponse;
import org.c4marathon.assignment.board.dto.PageInfo;
import org.c4marathon.assignment.board.exception.NotFoundBoardException;
import org.c4marathon.assignment.global.utils.PageTokenUtils;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BoardRepository {

	private final BoardJpaRepository boardJpaRepository;

	public Boards getById(Long id) {
		return boardJpaRepository.findById(id)
			.orElseThrow(() -> new NotFoundBoardException());
	}

	public Boards save(Boards boards) {
		return boardJpaRepository.save(boards);
	}

	public PageInfo<BoardGetAllResponse> findBoardsWithoutPageToken(int size) {
		List<BoardGetAllResponse> data = boardJpaRepository.findBoards(size + 1);

		return PageInfo.of(data, size, BoardGetAllResponse::createdDate, BoardGetAllResponse::id);
	}

	public PageInfo<BoardGetAllResponse> findBoardsWithPageToken(String pageToken, int size) {
		var pageData = PageTokenUtils.decodePageToken(pageToken, LocalDateTime.class, Long.class);
		var createdDate = pageData.getLeft();
		var boardId = pageData.getRight();

		List<BoardGetAllResponse> data = boardJpaRepository.findBoardsWithPageToken(createdDate, boardId, size + 1);

		return PageInfo.of(data, size, BoardGetAllResponse::createdDate, BoardGetAllResponse::id);
	}
}
