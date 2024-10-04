package org.c4marathon.assignment.auth.filter;

import static org.c4marathon.assignment.auth.util.AuthTokenContext.AUTHORITIES;
import static org.c4marathon.assignment.auth.util.AuthTokenContext.MEMBER_ID;
import static org.c4marathon.assignment.global.exception.exceptioncode.ExceptionCode.INVALID_REQUEST;
import static org.c4marathon.assignment.global.exception.exceptioncode.ExceptionCode.REQUEST_LOGIN;

import java.io.IOException;

import org.c4marathon.assignment.auth.util.AuthTokenContext;
import org.c4marathon.assignment.auth.util.TokenHandler;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AccessTokenValidatorFilter extends OncePerRequestFilter {

    private final AuthTokenContext authTokenContext;
    private final TokenHandler tokenHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String accessToken = request.getHeader(authTokenContext.getAuthHeader());
        if (accessToken != null) {
            try {
                createAuthenticationContext(accessToken);
            } catch (ExpiredJwtException e) {
                tokenHandler.handleException(response, REQUEST_LOGIN);
                return;
            } catch (SignatureException e) {
                tokenHandler.handleException(response, INVALID_REQUEST);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * 회원 가입, 로그인, 재발급 땐 제외함.
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.equals("/login")
                || path.equals("/members")
                || path.equals("/merchant/members")
                || path.equals("/reissue");
    }

    private void createAuthenticationContext(String accessToken) {
        Claims claims = tokenHandler.getClaims(accessToken);
        String memberId = String.valueOf(claims.get(MEMBER_ID));
        String authorities = String.valueOf(claims.get(AUTHORITIES));

        tokenHandler.createAuthenticationContext(memberId, authorities);
    }

}
