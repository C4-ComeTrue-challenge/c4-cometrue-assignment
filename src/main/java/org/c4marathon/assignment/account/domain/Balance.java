package org.c4marathon.assignment.account.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.c4marathon.assignment.global.exception.AccountException;

import static lombok.AccessLevel.PROTECTED;
import static org.c4marathon.assignment.global.exception.exceptioncode.ExceptionCode.ACCOUNT_BALANCE_NOT_ENOUGH;
import static org.c4marathon.assignment.global.exception.exceptioncode.ExceptionCode.BALANCE_CANNOT_NEGATIVE;

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

    public Balance withdraw(Long amount) {
        if (balance - amount < 0) {
            throw new AccountException(ACCOUNT_BALANCE_NOT_ENOUGH);
        }
        balance -= amount;
        return this;
    }

    public Balance deposit(Long amount) {
        balance += amount;
        return this;
    }

    public Boolean notEnoughMoney(Long money) {
        return balance < money;
    }
}
