package org.c4marathon.assignment.board.domain.repository;

import static org.c4marathon.assignment.board.domain.QBoards.*;

import java.time.LocalDateTime;
import java.util.List;

import org.c4marathon.assignment.board.dto.BoardGetAllResponse;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class BoardCustomRepositoryImpl implements BoardCustomRepository {

	private final JPAQueryFactory queryFactory;

	public BoardCustomRepositoryImpl(JPAQueryFactory queryFactory) {
		this.queryFactory = queryFactory;
	}

	@Override
	public List<BoardGetAllResponse> findBoards(int limit) {
		return queryFactory.select(
				Projections.constructor(BoardGetAllResponse.class, boards.id, boards.title, boards.content,
					boards.writerName, boards.createdDate, boards.lastModifiedDate))
			.from(boards)
			.orderBy(boards.createdDate.desc(), boards.id.asc())
			.limit(limit)
			.fetch();
	}

	@Override
	public List<BoardGetAllResponse> findBoardsWithPageToken(LocalDateTime createdDate, Long id, int limit) {
		return queryFactory.select(
				Projections.constructor(BoardGetAllResponse.class,
					boards.id, boards.title, boards.content,
					boards.writerName, boards.createdDate, boards.lastModifiedDate))
			.from(boards)
			.where(boards.createdDate.lt(createdDate)
				.or(boards.createdDate.eq(createdDate).and(boards.id.gt(id))))
			.orderBy(boards.createdDate.desc(), boards.id.asc())
			.limit(limit)
			.fetch();
	}

}
