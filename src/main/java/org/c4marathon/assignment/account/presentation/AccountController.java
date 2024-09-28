package org.c4marathon.assignment.account.presentation;

import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.account.dto.response.AccountDto;
import org.c4marathon.assignment.account.service.AccountService;
import org.c4marathon.assignment.member.domain.MemberAuthority;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static org.c4marathon.assignment.member.domain.MemberAuthority.CUSTOMER;
import static org.c4marathon.assignment.member.domain.MemberAuthority.MERCHANT;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping
    public ResponseEntity<AccountDto> getAccountInfo(
            @RequestParam(required = false) Long transactionId,
            Authentication authentication
    ) {
        Long memberAuthId = getMemberAuthId(authentication);
        MemberAuthority authority = getAuthority(authentication);
        AccountDto accountDto = accountService.showAccountInfo(authority, memberAuthId, transactionId);

        return ResponseEntity.ok(accountDto);
    }

    private static MemberAuthority getAuthority(Authentication authentication) {
        return authentication.getAuthorities().toString().contains("MERCHANT") ? MERCHANT : CUSTOMER;
    }

    private static long getMemberAuthId(Authentication auth) {
        return Long.parseLong(auth.getName());
    }
}
