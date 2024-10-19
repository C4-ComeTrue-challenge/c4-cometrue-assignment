package org.c4marathon.assignment.img.domain.repository;

import java.util.List;

import org.c4marathon.assignment.board.domain.Boards;

public interface ImgCustomRepository {
	List<String> findFileNamesByBoardId(Long boardId);

	void deleteByFileNames(List<String> fileNames);

	void updateBoardByFileNames(List<String> fileNames, Boards board);
}
