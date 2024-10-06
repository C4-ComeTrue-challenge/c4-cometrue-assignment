package org.c4marathon.assignment.board.domain.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.c4marathon.assignment.board.dto.BoardGetAllResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.c4marathon.assignment.board.domain.QBoard.board;

public class BoardCustomRepositoryImpl implements BoardCustomRepository {

    private final JPAQueryFactory queryFactory;

    public BoardCustomRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<BoardGetAllResponse> findAllWithPaging(Pageable pageable) {

        List<BoardGetAllResponse> results = queryFactory
                .select(Projections.constructor(BoardGetAllResponse.class,
                        board.content,
                        board.writerName,
                        board.createdDate,
                        board.lastModifiedDate))
                .from(board)
                .orderBy(board.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(board.count())
                .from(board)
                .fetchOne();

        return new PageImpl<>(results, pageable, total);
    }


}
