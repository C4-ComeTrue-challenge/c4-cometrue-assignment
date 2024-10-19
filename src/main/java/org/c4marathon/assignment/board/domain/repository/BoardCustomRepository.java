package org.c4marathon.assignment.board.domain.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.c4marathon.assignment.board.dto.BoardGetAllResponse;

public interface BoardCustomRepository {

	List<BoardGetAllResponse> findBoards(int limit);

	List<BoardGetAllResponse> findBoardsWithPageToken(LocalDateTime createdDate, Long id, int limit);

}
