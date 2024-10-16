package org.c4marathon.assignment.board.domain.repository;

import static org.c4marathon.assignment.board.domain.QBoards.*;

import java.util.List;

import org.c4marathon.assignment.board.dto.BoardGetAllResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class BoardCustomRepositoryImpl implements BoardCustomRepository {

	private final JPAQueryFactory queryFactory;

	public BoardCustomRepositoryImpl(JPAQueryFactory queryFactory) {
		this.queryFactory = queryFactory;
	}

	@Override
	public Page<BoardGetAllResponse> findAllWithPaging(Pageable pageable) {

		List<BoardGetAllResponse> results = queryFactory.select(
				Projections.constructor(BoardGetAllResponse.class, boards.content, boards.writerName, boards.createdDate,
					boards.lastModifiedDate))
			.from(boards)
			.orderBy(boards.createdDate.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		long total = queryFactory.select(boards.count()).from(boards).fetchOne();

		return new PageImpl<>(results, pageable, total);
	}

}
