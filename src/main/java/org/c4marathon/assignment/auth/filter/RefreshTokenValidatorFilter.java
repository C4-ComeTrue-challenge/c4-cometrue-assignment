package org.c4marathon.assignment.auth.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.auth.domain.AuthTokenContext;
import org.c4marathon.assignment.auth.domain.Session;
import org.c4marathon.assignment.auth.domain.repository.SessionRepository;
import org.c4marathon.assignment.auth.util.TokenHandler;
import org.c4marathon.assignment.global.exception.AuthException;
import org.c4marathon.assignment.member.domain.Member;
import org.c4marathon.assignment.member.domain.repository.MemberRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static java.lang.Boolean.TRUE;
import static org.c4marathon.assignment.auth.domain.AuthTokenContext.MEMBER_ID;
import static org.c4marathon.assignment.global.exception.exceptioncode.ExceptionCode.*;

@RequiredArgsConstructor
public class RefreshTokenValidatorFilter extends OncePerRequestFilter {

    private final AuthTokenContext authTokenContext;
    private final TokenHandler tokenHandler;
    private final MemberRepository memberRepository;
    private final SessionRepository sessionRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String refreshToken = request.getHeader(authTokenContext.getAuthHeader());
            isExistRefreshToken(refreshToken);

            Long memberId = getMemberIdFrom(refreshToken);
            Session findSession = sessionRepository.findByMemberId(memberId);
            isExistSession(findSession);
            isBlackList(findSession);
            isCurrentToken(findSession, refreshToken);

            Member findMember = memberRepository.findById(memberId)
                                                .orElseThrow(() -> new AuthException(MEMBER_NOT_FOUND));

            Authentication auth = generateAuthenticationBy(findMember);
            SecurityContextHolder.getContext().setAuthentication(auth);
        } catch (ExpiredJwtException | BadCredentialsException e) {
            tokenHandler.handleException(response, REQUEST_LOGIN);
            return;
        } catch (SignatureException e) {
            tokenHandler.handleException(response, INVALID_REQUEST);
            return;
        } catch (AuthException e) {
            tokenHandler.handleException(response, MEMBER_NOT_FOUND);
        }

        filterChain.doFilter(request, response);
    }

    private static UsernamePasswordAuthenticationToken generateAuthenticationBy(Member findMember) {
        return new UsernamePasswordAuthenticationToken(findMember.getId(), null,
                AuthorityUtils.commaSeparatedStringToAuthorityList(findMember.getAuthority().toString()));
    }

    // 재발급 상황에서만 사용함.
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getRequestURI().equals("/reissue");
    }

    private static void isCurrentToken(Session findSession, String refreshToken) {
        if (!findSession.getRefreshToken().equals(refreshToken)) {
            throw new BadCredentialsException("다시 로그인 해주세요.");
        }
    }

    private static void isBlackList(Session findSession) {
        if (findSession.getIsBlackList() == TRUE) {
            throw new BadCredentialsException("다시 로그인 해주세요.");
        }
    }

    private Long getMemberIdFrom(String refreshToken) {
        Claims claims = tokenHandler.getClaims(refreshToken);
        return Long.parseLong((String)claims.get(MEMBER_ID));
    }

    private static void isExistRefreshToken(String refreshToken) {
        if (refreshToken == null) {
            throw new BadCredentialsException("다시 로그인해주세요.");
        }
    }

    private static void isExistSession(Session findSession) {
        if (findSession == null) {
            throw new BadCredentialsException("다시 로그인해주세요.");
        }
    }

}
