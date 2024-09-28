package org.c4marathon.assignment.account.presentation;

import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.account.dto.response.AccountResponse;
import org.c4marathon.assignment.account.service.CommonAccountService;
import org.c4marathon.assignment.global.exception.AccountException;
import org.c4marathon.assignment.member.domain.MemberAuthority;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static org.c4marathon.assignment.global.exception.exceptioncode.ExceptionCode.NO_AUTHORITY;
import static org.c4marathon.assignment.member.domain.MemberAuthority.CUSTOMER;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class CustomerAccountController {

    private final CommonAccountService commonAccountService;

    @GetMapping
    public ResponseEntity<AccountResponse> getAccountInfo(
            @RequestParam(required = false) Long transactionId,
            Authentication authentication
    ) {
        Long memberAuthId = getMemberAuthId(authentication);
        MemberAuthority authority = getAuthority(authentication);
        AccountResponse accountDto = commonAccountService.showAccountInfo(authority, memberAuthId, transactionId);

        return ResponseEntity.ok(accountDto);
    }

    private static MemberAuthority getAuthority(Authentication authentication) {
        if (authentication.getAuthorities().toString().contains("CUSTOMER")) {
            return CUSTOMER;
        } else {
            throw new AccountException(NO_AUTHORITY);
        }
    }

    private static long getMemberAuthId(Authentication auth) {
        return Long.parseLong(auth.getName());
    }
}
