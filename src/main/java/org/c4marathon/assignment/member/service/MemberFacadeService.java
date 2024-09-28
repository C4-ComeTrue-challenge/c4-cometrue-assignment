package org.c4marathon.assignment.member.service;

import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.account.domain.Balance;
import org.c4marathon.assignment.account.service.CommonAccountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
