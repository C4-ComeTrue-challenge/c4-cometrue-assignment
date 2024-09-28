package org.c4marathon.assignment.auth.service;

import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.auth.domain.Session;
import org.c4marathon.assignment.auth.domain.repository.SessionRepository;
import org.c4marathon.assignment.auth.util.TokenHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.c4marathon.assignment.auth.domain.AuthTokenContext.MEMBER_ID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final SessionRepository sessionRepository;
    private final TokenHandler tokenHandler;

    @Transactional
    public void storeRefreshToken(String refreshToken) {
        Long memberId = extractEmailFrom(refreshToken);
        Session findSession = sessionRepository.findByMemberId(memberId);

        if (findSession == null) {
            sessionRepository.save(new Session(refreshToken, memberId));
        }
        else {
            findSession.updateRefreshToken(refreshToken);
        }
    }

    @Transactional
    public void blackSessionBy(Long userId) {
        Session findSession = sessionRepository.findByMemberId(userId);
        if (findSession == null) {
            return;
        }

        findSession.blackSession();
    }

    private Long extractEmailFrom(String refreshToken) {
        return Long.parseLong(tokenHandler.getClaims(refreshToken).get(MEMBER_ID).toString());
    }

}
