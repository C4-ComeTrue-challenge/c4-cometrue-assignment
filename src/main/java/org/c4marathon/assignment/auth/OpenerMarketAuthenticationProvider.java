package org.c4marathon.assignment.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.c4marathon.assignment.global.exception.AuthException;
import org.c4marathon.assignment.member.domain.Member;
import org.c4marathon.assignment.member.domain.repository.CustomerRepository;
import org.c4marathon.assignment.member.domain.repository.MemberRepository;
import org.c4marathon.assignment.member.domain.repository.MerchantRepository;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.c4marathon.assignment.global.exception.exceptioncode.ExceptionCode.MEMBER_NOT_FOUND;
import static org.c4marathon.assignment.member.domain.MemberAuthority.MERCHANT;

@Slf4j
@Component
@RequiredArgsConstructor
public class OpenerMarketAuthenticationProvider implements AuthenticationProvider {

    private final MemberRepository memberRepository;
    private final MerchantRepository merchantRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String nickname = authentication.getName();
        String password = authentication.getCredentials().toString();
        Member member = memberRepository.findByNickname(nickname)
                                        .orElseThrow(() -> new AuthException(MEMBER_NOT_FOUND));
        isPasswordMatch(password, member);

        Long memberAuthId = getMemberAuthId(member);

        return new UsernamePasswordAuthenticationToken(memberAuthId, password,
                List.of(new SimpleGrantedAuthority(member.getAuthority().toString())));
    }

    private Long getMemberAuthId(Member member) {
        Long id;
        if (member.getAuthority().equals(MERCHANT)) {
            id = merchantRepository.findById(member.getId()).get().getId();
        } else {
            id = customerRepository.findById(member.getId()).get().getId();
        }
        return id;
    }

    private void isPasswordMatch(String password, Member member) {
        if (!passwordEncoder.matches(password, member.getPassword()))
            throw new UsernameNotFoundException(MEMBER_NOT_FOUND.getMessage());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }

}
