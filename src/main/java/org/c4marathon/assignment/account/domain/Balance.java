package org.c4marathon.assignment.account.domain;

import static lombok.AccessLevel.PROTECTED;
import static org.c4marathon.assignment.global.exception.exceptioncode.ExceptionCode.ACCOUNT_BALANCE_NOT_ENOUGH;
import static org.c4marathon.assignment.global.exception.exceptioncode.ExceptionCode.BALANCE_CANNOT_NEGATIVE;

import org.c4marathon.assignment.global.exception.AccountException;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = PROTECTED)
public class Balance {

    @Getter
    private Long balance;

    public Balance(Long balance) {
        if (balance < 0) {
            throw new AccountException(BALANCE_CANNOT_NEGATIVE);
        }
        this.balance = balance;
    }

    public void withdraw(Long amount) {
        if (balance - amount < 0) {
            throw new AccountException(ACCOUNT_BALANCE_NOT_ENOUGH);
        }
        balance -= amount;
    }

    public void deposit(Long amount) {
        balance += amount;
    }

    public Boolean hasEnoughMoney(Long money) {
        return balance >= money;
    }
}
