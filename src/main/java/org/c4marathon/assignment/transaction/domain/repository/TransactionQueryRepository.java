package org.c4marathon.assignment.transaction.domain.repository;

import static java.util.Comparator.comparing;
import static org.c4marathon.assignment.global.utils.PageUtil.SMALL_PAGE_SIZE;
import static org.c4marathon.assignment.transaction.domain.QTransaction.transaction;

import java.time.LocalDateTime;
import java.util.List;

import org.c4marathon.assignment.transaction.dto.TransactionDto;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TransactionQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<TransactionDto> getTransactionsByAccountIdAndTransactionDate(Long accountId, LocalDateTime cursorDate) {

        BooleanBuilder dynamicLtCursorDate = new BooleanBuilder();
        if (cursorDate != null) {
            dynamicLtCursorDate.and(transaction.transactionDate.lt(cursorDate));
        }

        List<TransactionDto> fromAccountResult = queryFactory
                .select(Projections.constructor(TransactionDto.class,
                        transaction.id,
                        transaction.fromNickname,
                        transaction.toNickname,
                        transaction.amount,
                        transaction.balance,
                        transaction.memo,
                        transaction.transactionDate))
                .from(transaction)
                .where(dynamicLtCursorDate
                        .and(transaction.fromAccountId.eq(accountId)))
                .orderBy(transaction.fromAccountId.asc(), transaction.transactionDate.desc())
                .limit(SMALL_PAGE_SIZE + 1)
                .fetch();

        List<TransactionDto> toAccountResult = queryFactory
                .select(Projections.constructor(TransactionDto.class,
                        transaction.id,
                        transaction.fromNickname,
                        transaction.toNickname,
                        transaction.amount,
                        transaction.balance,
                        transaction.memo,
                        transaction.transactionDate))
                .from(transaction)
                .where(dynamicLtCursorDate
                        .and(transaction.toAccountId.eq(accountId)))
                .orderBy(transaction.toAccountId.asc(), transaction.transactionDate.desc())
                .limit(SMALL_PAGE_SIZE + 1)
                .fetch();

        fromAccountResult.addAll(toAccountResult);
        return fromAccountResult.stream()
                                .sorted(comparing(TransactionDto::transactionDate))
                                .limit(SMALL_PAGE_SIZE + 1)
                                .toList();
    }
}
