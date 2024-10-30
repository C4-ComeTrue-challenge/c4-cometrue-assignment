package org.c4marathon.assignment.account.presentation;

import org.c4marathon.assignment.account.dto.response.AccountResponse;
import org.c4marathon.assignment.account.service.CommonAccountService;
import org.c4marathon.assignment.global.annotation.AuthMember;
import org.c4marathon.assignment.member.domain.MemberAuthority;
import org.c4marathon.assignment.member.dto.AuthMemberDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class CustomerAccountController {

    private final CommonAccountService commonAccountService;

    @GetMapping
    public ResponseEntity<AccountResponse> getAccountInfo(
            @RequestParam(required = false) Long transactionId,
            @AuthMember AuthMemberDto authMember
    ) {
        Long memberAuthId = authMember.memberId();
        MemberAuthority authority = authMember.getAuthority();
        AccountResponse accountDto = commonAccountService.showAccountInfo(authority, memberAuthId, transactionId);

        return ResponseEntity.ok(accountDto);
    }

}
