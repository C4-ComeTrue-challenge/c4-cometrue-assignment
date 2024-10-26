package org.c4marathon.assignment.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity { // 댓글과 답글을 단일 테이블 전략으로 관리
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent; // 부모 댓글을 참조하는 컬럼

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Comment> replies = new ArrayList<>();

    // 비회원용 닉네임
    @Column(nullable = true)
    private String nickname;

    // 비회원용 비밀번호
    @Column(nullable = true)
    private String password;

    @Column(nullable = false)
    private boolean deleted = false; // 소프트 삭제를 위한 필드

    @Builder
    public Comment(String content, Member member, Post post, Comment parent,String nickname,String password) {
        this.content = content;
        this.member = member;
        this.post = post;
        this.parent = parent;
        this.nickname=nickname;
        this.password=password;
    }

    public boolean isWrittenBy(Member member) {
        return this.member != null && this.member.equals(member);
    }

    public boolean isWrittenByGuest(String nickname, String password) {
        return this.nickname != null && this.nickname.equals(nickname) && this.password.equals(password);
    }

    public void update(String content) {
        this.content = content;
    }

    public void delete() {
        this.deleted = true;
    }
}
