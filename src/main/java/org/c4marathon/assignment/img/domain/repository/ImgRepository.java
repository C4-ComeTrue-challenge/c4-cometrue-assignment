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

	public void save(String fileName) {
		imgJpaRepository.save(Img.builder().fileName(fileName).build());
	}

	public void setBoardByFileName(List<String> fileNames, Boards board) {
		List<Img> images = imgJpaRepository.findByFileNameIn(fileNames);

		images.forEach(img -> {
			img.setBoard(board);
			imgJpaRepository.save(img);
		});
	}

	public boolean existsByFileName(String fileName) {
		return imgJpaRepository.existsByFileName(fileName);
	}
}
