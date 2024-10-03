package org.c4marathon.assignment.auth.domain;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.c4marathon.assignment.member.domain.MemberAuthority;

import static jakarta.persistence.GenerationType.IDENTITY;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@EqualsAndHashCode
@ToString
@NoArgsConstructor(access = PROTECTED)
public class Session {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "session_id")
    private Long id;

    @Column(nullable = false)
    private String refreshToken;

    private Long memberAuthId;
    private MemberAuthority authority;
    private Boolean isBlackList;

    public Session(final String refreshToken, final MemberAuthority authority, final Long memberAuthId) {
        this.refreshToken = refreshToken;
        this.authority = authority;
        this.memberAuthId = memberAuthId;
        this.isBlackList = FALSE;
    }

    public static Session of(final String refreshToken, final MemberAuthority authority, final Long memberAuthId) {
        return new Session(refreshToken, authority, memberAuthId);
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void blackSession() {
        isBlackList = TRUE;
    }
}
