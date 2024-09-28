package org.c4marathon.assignment.account.domain.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.transaction.dto.TransactionDto;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.c4marathon.assignment.transaction.domain.QTransaction.transaction;
import static org.c4marathon.assignment.global.utils.PageUtil.SMALL_PAGE_SIZE;

@Repository
@RequiredArgsConstructor
public class AccountQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;

    public List<TransactionDto> getTransactions(Long accountId, Long transactionCursorId) {

        BooleanBuilder dynamicLtTransactionId = new BooleanBuilder();

        if (transactionCursorId != null) {
            dynamicLtTransactionId.and(transaction.id.lt(transactionCursorId));
        }

        return queryFactory
                .select(Projections.constructor(TransactionDto.class,
                        transaction.fromNickname,
                        transaction.toNickname,
                        transaction.money,
                        transaction.transactionDate))
                .from(transaction)
                .where(dynamicLtTransactionId
                        .and(transaction.account.id.eq(accountId)))
                .orderBy(transaction.account.id.asc(), transaction.id.desc())
                .limit(SMALL_PAGE_SIZE)
                .fetch();
    }
}
