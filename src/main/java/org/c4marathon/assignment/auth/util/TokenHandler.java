package org.c4marathon.assignment.auth.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.auth.domain.AuthTokenContext;
import org.c4marathon.assignment.global.exception.exceptioncode.ExceptionCode;
import org.c4marathon.assignment.global.exception.response.ErrorResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.security.core.authority.AuthorityUtils.commaSeparatedStringToAuthorityList;

@Component
@RequiredArgsConstructor
public class TokenHandler {

    private final AuthTokenContext authTokenContext;
    private final ObjectMapper objectMapper;

    public String mergeAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Set<String> authoritiesSet = new HashSet<>();
        authorities.forEach(authority -> authoritiesSet.add(authority.getAuthority()));

        return String.join(",", authoritiesSet);
    }

    public void createAuthenticationContext(String memberId, String authorities) {
        Authentication auth = generateAuth(memberId, authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    public Claims getClaims(String jwtAccessToken) {
        return Jwts.parserBuilder()
                .setSigningKey(authTokenContext.getSecretKey()).build()
                .parseClaimsJws(jwtAccessToken).getBody();
    }

    public void handleException(HttpServletResponse response, ExceptionCode exceptionCode) throws IOException {
        response.setStatus(exceptionCode.getCode());
        response.setContentType("application/json");

        objectMapper.writeValue(response.getWriter(),
                new ErrorResponse(exceptionCode.getCode(), exceptionCode.getMessage()));
    }

    private static UsernamePasswordAuthenticationToken generateAuth(String memberId, String authorities) {
        return new UsernamePasswordAuthenticationToken(memberId, null,
                commaSeparatedStringToAuthorityList(authorities));
    }

}
