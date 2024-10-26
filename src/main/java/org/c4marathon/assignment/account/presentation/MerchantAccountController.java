package org.c4marathon.assignment.account.presentation;

import static org.c4marathon.assignment.global.exception.exceptioncode.ExceptionCode.NO_AUTHORITY;
import static org.c4marathon.assignment.member.domain.MemberAuthority.MERCHANT;

import org.c4marathon.assignment.account.dto.response.AccountResponse;
import org.c4marathon.assignment.account.service.CommonAccountService;
import org.c4marathon.assignment.global.annotation.AuthMember;
import org.c4marathon.assignment.global.exception.AccountException;
import org.c4marathon.assignment.member.domain.MemberAuthority;
import org.c4marathon.assignment.member.dto.AuthMemberDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/merchant/accounts")
@RequiredArgsConstructor
public class MerchantAccountController {

    private final CommonAccountService accountService;

    @GetMapping
    public ResponseEntity<AccountResponse> getAccountInfo(
            @RequestParam(required = false) Long transactionId,
            @AuthMember AuthMemberDto authMember
    ) {
        Long memberAuthId = authMember.memberId();
        MemberAuthority authority = authMember.getAuthority();
        AccountResponse accountDto = accountService.showAccountInfo(authority, memberAuthId, transactionId);
        return ResponseEntity.ok(accountDto);
    }

}
