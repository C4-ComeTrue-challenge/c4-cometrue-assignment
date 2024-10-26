package org.c4marathon.assignment.transaction.controller;

import static org.c4marathon.assignment.member.domain.MemberAuthority.CUSTOMER;
import static org.c4marathon.assignment.member.domain.MemberAuthority.MERCHANT;

import org.c4marathon.assignment.account.domain.Account;
import org.c4marathon.assignment.account.service.CommonAccountService;
import org.c4marathon.assignment.global.annotation.AuthMember;
import org.c4marathon.assignment.member.domain.MemberAuthority;
import org.c4marathon.assignment.member.dto.AuthMemberDto;
import org.c4marathon.assignment.transaction.dto.ChargeRequest;
import org.c4marathon.assignment.transaction.service.ChargeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/charges")
@RequiredArgsConstructor
public class ChargeController {

    private final CommonAccountService commonAccountService;
    private final ChargeService chargeService;

    @PostMapping
    public ResponseEntity<Void> chargeAccount(
            @RequestBody final ChargeRequest chargeRequest,
            @AuthMember AuthMemberDto authMember
    ) {
        Account account = getAccount(authMember);
        chargeService.chargeCash(account.getId(), chargeRequest.money());
        return ResponseEntity.ok().build();
    }

    private Account getAccount(AuthMemberDto authMember) {
        MemberAuthority authority = authMember.getAuthority();
        return commonAccountService.findAccountByAuthorityAndMemberAuthId(authority, authMember.memberId());
    }

}
