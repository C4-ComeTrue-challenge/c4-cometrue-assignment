package org.c4marathon.assignment.member.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;
import static org.c4marathon.assignment.member.domain.MemberAuthority.CUSTOMER;
import static org.c4marathon.assignment.member.domain.MemberAuthority.MERCHANT;

@Entity
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Member {

    @Id
    @Getter
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Getter
    private Long merchantId;

    @Getter
    private Long customerId;

    @Getter
    @Column(nullable = false, unique = true, length = 20)
    private String nickname;

    @Getter
    @Column(nullable = false)
    private String password;

    @Getter
    @Enumerated(STRING)
    private MemberAuthority authority;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    private Member(final String nickname, final MemberAuthority authority, final String password) {
        this.nickname = nickname;
        this.authority = authority;
        this.password = password;
    }

    public static Member customer(String nickname, String password) {
        return new Member(nickname, CUSTOMER, password);
    }

    public static Member merchant(String nickname, String password) {
        return new Member(nickname, MERCHANT, password);
    }

    public void addMerchant(Long merchantId) {
        this.merchantId = merchantId;
    }

    public void addCustomer(Long customerId) {
        this.customerId = customerId;
    }
}
