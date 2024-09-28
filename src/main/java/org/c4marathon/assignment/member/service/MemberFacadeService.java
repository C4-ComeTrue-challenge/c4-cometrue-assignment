package org.c4marathon.assignment.member.service;

import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.account.domain.Balance;
import org.c4marathon.assignment.account.service.AccountService;
import org.c4marathon.assignment.member.domain.Member;
import org.c4marathon.assignment.member.domain.MemberAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberFacadeService {

    private final MemberService memberService;
    private final AccountService accountService;

    @Transactional
    public void createMemberAndAccount(String nickname, String password, MemberAuthority authority) {
        Long memberAuthId = memberService.registerUser(nickname, password, authority);
        accountService.createAccount(nickname, new Balance(0L), authority, memberAuthId);
    }
}
