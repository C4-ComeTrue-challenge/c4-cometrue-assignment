package org.c4marathon.assignment.auth.config;

import org.c4marathon.assignment.auth.util.AuthTokenContext;
import org.c4marathon.assignment.auth.domain.repository.SessionRepository;
import org.c4marathon.assignment.auth.filter.AccessTokenValidatorFilter;
import org.c4marathon.assignment.auth.filter.AuthTokenGenerateFilter;
import org.c4marathon.assignment.auth.filter.RefreshTokenValidatorFilter;
import org.c4marathon.assignment.auth.util.TokenHandler;
import org.c4marathon.assignment.member.domain.repository.MemberRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, AuthTokenContext authTokenContext,
                                            MemberRepository memberRepository, SessionRepository sessionRepository,
                                            TokenHandler tokenHandler) throws Exception {
        http.sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .httpBasic(Customizer.withDefaults())
                .addFilterAfter(new AuthTokenGenerateFilter(authTokenContext, tokenHandler),
                                    BasicAuthenticationFilter.class)
                .addFilterBefore(new AccessTokenValidatorFilter(authTokenContext, tokenHandler),
                                    BasicAuthenticationFilter.class)
                .addFilterBefore(new RefreshTokenValidatorFilter(authTokenContext, tokenHandler,
                                    memberRepository, sessionRepository), AccessTokenValidatorFilter.class)
                .csrf((csrf) -> csrf.disable())
                .authorizeHttpRequests((request) -> request
                        .requestMatchers(AuthenticationConfig.getAuthMatchers()).authenticated()
                        .requestMatchers(PermitAllConfig.getPermitAllMatchers()).permitAll());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
