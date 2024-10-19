package org.c4marathon.assignment.img.domain.repository;

import java.util.List;

import org.c4marathon.assignment.board.domain.Boards;
import org.c4marathon.assignment.img.domain.Img;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ImgRepository {
	private final ImgJpaRepository imgJpaRepository;

	public void save(Img img) {
		imgJpaRepository.save(img);
	}

	public void setBoardByFileName(List<String> fileNames, Boards board) {
		imgJpaRepository.updateBoardByFileNames(fileNames, board);
	}

	public boolean existsByFileName(String fileName) {
		return imgJpaRepository.existsByFileName(fileName);
	}

	public void deleteByFileNames(List<String> fileNames) {
		imgJpaRepository.deleteByFileNames(fileNames);
	}

	public List<String> getFileNamesByBoardId(Long boardId) {
		return imgJpaRepository.findFileNamesByBoardId(boardId);
	}
}
