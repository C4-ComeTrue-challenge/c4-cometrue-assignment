package org.c4marathon.assignment.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.c4marathon.assignment.global.exception.AuthException;
import org.c4marathon.assignment.user.domain.Member;
import org.c4marathon.assignment.user.domain.repository.MemberRepository;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.c4marathon.assignment.global.exception.exceptioncode.ExceptionCode.ACCOUNT_NOT_FOUND;

@Slf4j
@Component
@RequiredArgsConstructor
public class OpenerMarketAuthenticationProvider implements AuthenticationProvider {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Long userId = Long.parseLong(authentication.getName());
        String password = authentication.getCredentials().toString();
        Member member = memberRepository.findById(userId)
                                        .orElseThrow(() -> new AuthException(ACCOUNT_NOT_FOUND));

        if (!passwordEncoder.matches(password, member.getPassword()))
            throw new UsernameNotFoundException(ACCOUNT_NOT_FOUND.getMessage());

        return new UsernamePasswordAuthenticationToken(userId, password,
                List.of(new SimpleGrantedAuthority(member.getAuthority().toString())));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }

}
