package org.c4marathon.assignment.img.domain.repository;

import java.util.List;
import java.util.Optional;

import org.c4marathon.assignment.board.domain.Boards;
import org.c4marathon.assignment.img.domain.Img;

public interface ImgCustomRepository {
	List<String> findFileNamesByBoardId(Long boardId);

	void deleteByFileNames(List<String> fileNames);

	void updateBoardByFileNames(List<String> fileNames, Boards board);

	Optional<Img> findNotDeletedById(Long id);
}
