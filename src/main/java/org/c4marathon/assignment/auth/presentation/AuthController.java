package org.c4marathon.assignment.auth.presentation;

import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.auth.util.AuthTokenContext;
import org.c4marathon.assignment.auth.dto.TokenResponse;
import org.c4marathon.assignment.auth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AuthTokenContext authTokenContext;

    @RequestMapping("/login")
    public ResponseEntity<TokenResponse> loginMember() {
        TokenResponse tokens = authTokenContext.getCurrentToken();
        authService.storeRefreshToken(tokens.refreshToken());
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
    public ResponseEntity<Void> logoutMember(Authentication authentication) {
        Long userId = getUserIdFrom(authentication);
        authService.blackSessionBy(userId);
        return ResponseEntity.noContent().build();
    }

    private static Long getUserIdFrom(Authentication authentication) {
        return Long.parseLong(authentication.getName());
    }
}
