package org.c4marathon.assignment.auth.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Session {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "session_id")
    private Long id;

    @Column(nullable = false)
    private String refreshToken;

    private Long memberId;
    private Boolean isBlackList;

    public Session(String refreshToken, Long memberId) {
        this.refreshToken = refreshToken;
        this.memberId = memberId;
        this.isBlackList = FALSE;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void blackSession() {
        isBlackList = TRUE;
    }
}
