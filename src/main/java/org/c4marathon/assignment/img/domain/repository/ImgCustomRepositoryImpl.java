package org.c4marathon.assignment.img.domain.repository;

import static org.c4marathon.assignment.img.domain.QImg.*;

import java.util.List;

import org.c4marathon.assignment.board.domain.Boards;

import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

public class ImgCustomRepositoryImpl implements ImgCustomRepository {

	private final JPAQueryFactory queryFactory;
	private final EntityManager em;

	public ImgCustomRepositoryImpl(JPAQueryFactory queryFactory, EntityManager em) {
		this.queryFactory = queryFactory;
		this.em = em;
	}

	@Override
	public List<String> findFileNamesByBoardId(Long boardId) {
		return queryFactory
			.select(img.fileName)
			.from(img)
			.where(img.board.id.eq(boardId))
			.fetch();
	}

	@Override
	public void deleteByFileNames(List<String> fileNames) {
		long count = queryFactory
			.delete(img)
			.where(img.fileName.in(fileNames))
			.execute();

		em.flush();
		em.clear();
	}

	@Override
	public void updateBoardByFileNames(List<String> fileNames, Boards board) {
		long count = queryFactory
			.update(img)
			.set(img.board, board)
			.where(img.fileName.in(fileNames))
			.execute();
		em.flush();
		em.clear();
	}
}
