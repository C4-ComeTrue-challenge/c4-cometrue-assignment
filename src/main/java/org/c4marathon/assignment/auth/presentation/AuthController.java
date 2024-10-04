package org.c4marathon.assignment.auth.presentation;

import static org.c4marathon.assignment.member.domain.MemberAuthority.CUSTOMER;
import static org.c4marathon.assignment.member.domain.MemberAuthority.MERCHANT;

import org.c4marathon.assignment.auth.dto.TokenResponse;
import org.c4marathon.assignment.auth.service.AuthService;
import org.c4marathon.assignment.auth.util.AuthTokenContext;
import org.c4marathon.assignment.member.domain.MemberAuthority;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AuthTokenContext authTokenContext;

    @RequestMapping("/login")
    public ResponseEntity<TokenResponse> loginMember(
            Authentication authentication
    ) {
        TokenResponse tokens = authTokenContext.getCurrentToken();
        MemberAuthority authority = getMemberAuthorityFrom(authentication);
        authService.loginAndStoreRefreshToken(authority, tokens.refreshToken());
        authTokenContext.clearToken();
        return ResponseEntity.ok(tokens);
    }

    @GetMapping("/reissue")
    public ResponseEntity<TokenResponse> reissueExpiredToken() {
        TokenResponse tokens = authTokenContext.getCurrentToken();
        authTokenContext.clearToken();
        return ResponseEntity.ok(tokens);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logoutMember(
            Authentication authentication
    ) {
        Long memberAuthId = getUserIdFrom(authentication);
        MemberAuthority authority = getMemberAuthorityFrom(authentication);
        authService.blackSessionBy(authority, memberAuthId);
        return ResponseEntity.noContent().build();
    }

    private Long getUserIdFrom(Authentication authentication) {
        return Long.parseLong(authentication.getName());
    }

    private MemberAuthority getMemberAuthorityFrom(Authentication authentication) {
        if (authentication.getAuthorities().toString().contains("MERCHANT")) {
            return MERCHANT;
        } else {
            return CUSTOMER;
        }
    }
}
