package org.c4marathon.assignment.transaction.domain.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.transaction.dto.TransactionDto;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.c4marathon.assignment.transaction.domain.QTransaction.transaction;
import static org.c4marathon.assignment.global.utils.PageUtil.SMALL_PAGE_SIZE;

@Repository
@RequiredArgsConstructor
public class TransactionQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<TransactionDto> getTransactions(Long accountId, Long transactionCursorId) {

        BooleanBuilder dynamicLtTransactionId = new BooleanBuilder();

        if (transactionCursorId != null) {
            dynamicLtTransactionId.and(transaction.id.lt(transactionCursorId));
        }

        return queryFactory
                .select(Projections.constructor(TransactionDto.class,
                        transaction.id,
                        transaction.fromNickname,
                        transaction.toNickname,
                        transaction.money,
                        transaction.transactionDate))
                .from(transaction)
                .where(dynamicLtTransactionId
                        .and(transaction.account.id.eq(accountId)))
                .orderBy(transaction.account.id.asc(), transaction.id.desc())
                .limit(SMALL_PAGE_SIZE + 1)
                .fetch();
    }
}
