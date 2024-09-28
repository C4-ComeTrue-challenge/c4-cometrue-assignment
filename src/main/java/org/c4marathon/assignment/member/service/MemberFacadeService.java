package org.c4marathon.assignment.member.service;

import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.account.domain.Balance;
import org.c4marathon.assignment.account.service.CommonAccountService;
import org.c4marathon.assignment.member.domain.MemberAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberFacadeService {

    private final MemberService memberService;
    private final CommonAccountService accountService;

    @Transactional
    public void createCustomerMemberAndAccount(String nickname, String password, MemberAuthority authority) {
        Long memberAuthId = memberService.registerUser(nickname, password, authority);
        accountService.createCustomerAccount(nickname, new Balance(0L), memberAuthId);
    }

    @Transactional
    public void createMerchantMemberAndAccount(String nickname, String password, MemberAuthority authority) {
        Long memberAuthId = memberService.registerUser(nickname, password, authority);
        accountService.createMerchantAccount(nickname, new Balance(0L), memberAuthId);
    }
}
