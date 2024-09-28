package org.c4marathon.assignment.account.domain;

import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;
import org.c4marathon.assignment.global.exception.AccountException;

import static lombok.AccessLevel.PROTECTED;
import static org.c4marathon.assignment.global.exception.exceptioncode.ExceptionCode.BALANCE_CANNOT_NEGATIVE;

@Embeddable
@NoArgsConstructor(access = PROTECTED)
public class Balance {

    private Long balance;

    public Balance(Long balance) {
        if (balance < 0) {
            throw new AccountException(BALANCE_CANNOT_NEGATIVE);
        }
        this.balance = balance;
    }

    public void withdraw(Long amount) {
        if (balance - amount < 0) {
            throw new AccountException(BALANCE_CANNOT_NEGATIVE);
        }
        balance -= amount;
    }

    public void deposit(Long amount) {
        balance += amount;
    }
}
