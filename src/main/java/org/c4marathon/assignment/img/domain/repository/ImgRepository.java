package org.c4marathon.assignment.img.domain.repository;

import java.util.List;

import org.c4marathon.assignment.board.domain.Boards;
import org.c4marathon.assignment.img.domain.Img;
import org.c4marathon.assignment.img.exception.NotFoundImgException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ImgRepository {

	private final ImgJpaRepository imgJpaRepository;
	private final EntityManager entityManager;

	public Img save(Img img) {
		return imgJpaRepository.save(img);
	}

	public Img getById(Long id) {
		return imgJpaRepository.findNotDeletedById(id)
			.orElseThrow(() -> new NotFoundImgException());
	}

	@Transactional
	public void saveAll(List<Img> images) {
		final int BATCH_SIZE = 100;
		for (int i = 0; i < images.size(); i++) {
			entityManager.persist(images.get(i));
			if (i % BATCH_SIZE == 0) {
				entityManager.flush();
				entityManager.clear();
			}
		}
	}

	public void setBoardByFileName(List<String> fileNames, Boards board) {
		imgJpaRepository.updateBoardByFileNames(fileNames, board);
	}

	@Transactional
	public void deleteByFileNames(List<String> fileNames) {
		imgJpaRepository.deleteByFileNames(fileNames);
	}

	public List<String> getFileNamesByBoardId(Long boardId) {
		return imgJpaRepository.findFileNamesByBoardId(boardId);
	}
}
