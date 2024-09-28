package org.c4marathon.assignment.member.service;

import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.account.domain.Balance;
import org.c4marathon.assignment.account.service.CommonAccountService;
import org.c4marathon.assignment.global.exception.AuthException;
import org.c4marathon.assignment.global.exception.exceptioncode.ExceptionCode;
import org.c4marathon.assignment.member.domain.MemberAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.c4marathon.assignment.global.exception.exceptioncode.ExceptionCode.INVALID_REQUEST;
import static org.c4marathon.assignment.member.domain.MemberAuthority.CUSTOMER;
import static org.c4marathon.assignment.member.domain.MemberAuthority.MERCHANT;

@Service
@RequiredArgsConstructor
public class MemberFacadeService {

    private final MemberService memberService;
    private final CommonAccountService accountService;

    @Transactional
    public void createCustomerMemberAndAccount(String nickname, String password) {
        Long memberAuthId = memberService.registerCustomerUser(nickname, password);
        accountService.createCustomerAccount(nickname, new Balance(0L), memberAuthId);
    }

    @Transactional
    public void createMerchantMemberAndAccount(String nickname, String password) {
        Long memberAuthId = memberService.registerMerchantUser(nickname, password);
        accountService.createMerchantAccount(nickname, new Balance(0L), memberAuthId);
    }

}
