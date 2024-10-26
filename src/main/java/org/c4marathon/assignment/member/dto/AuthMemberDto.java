package org.c4marathon.assignment.member.dto;

import org.c4marathon.assignment.global.exception.AuthException;
import org.c4marathon.assignment.member.domain.MemberAuthority;

import static org.c4marathon.assignment.global.exception.exceptioncode.ExceptionCode.NO_AUTHORITY;
import static org.c4marathon.assignment.member.domain.MemberAuthority.CUSTOMER;
import static org.c4marathon.assignment.member.domain.MemberAuthority.MERCHANT;

public record AuthMemberDto(
        Long memberId,
        String authority
) {
    public MemberAuthority getAuthority() {
        if (this.authority().contains("MERCHANT")) {
            return MERCHANT;
        } else {
            return CUSTOMER;
        }
    }

    public void checkMerchant() {
        if (!this.authority().contains("MERCHANT")) {
            throw new AuthException(NO_AUTHORITY);
        }
    }

    public void validateCustomer() {
        if (!this.authority().contains("CUSTOMER")) {
            throw new AuthException(NO_AUTHORITY);
        }
    }
}
