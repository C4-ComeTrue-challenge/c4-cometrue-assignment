package org.c4marathon.assignment.auth.filter;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.auth.util.AuthTokenContext;
import org.c4marathon.assignment.auth.util.TokenHandler;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;

import static org.c4marathon.assignment.auth.util.AuthTokenContext.*;

@RequiredArgsConstructor
public class AuthTokenGenerateFilter extends OncePerRequestFilter {

    private final AuthTokenContext tokenContext;
    private final TokenHandler tokenHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            String accessToken = generateAccessToken(authentication);
            String refreshToken = generateRefreshToken(authentication);
            tokenContext.storeCurrentToken(accessToken, refreshToken);
        }

        filterChain.doFilter(request, response);
    }

    /**
     * 로그인과 재발급 때를 제외하고는 여기를 접근하면 안된다.
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return !(path.equals("/login") || path.equals("/reissue"));
    }

    private String generateAccessToken(Authentication authentication) {
        return Jwts.builder()
                .claim(MEMBER_ID, authentication.getName())
                .claim(AUTHORITIES, tokenHandler.mergeAuthorities(authentication.getAuthorities()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + ACCESS_TOKEN_EXPIRATION_TIME))
                .signWith(tokenContext.getSecretKey()).compact();
    }

    private String generateRefreshToken(Authentication authentication) {
        return Jwts.builder()
                .claim(MEMBER_ID, authentication.getName())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + REFRESH_TOKEN_EXPIRATION_TIME))
                .signWith(tokenContext.getSecretKey()).compact();
    }
}
