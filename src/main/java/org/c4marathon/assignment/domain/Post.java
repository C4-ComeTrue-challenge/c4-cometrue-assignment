package org.c4marathon.assignment.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = true)
    private Member member;

    // 비회원용 닉네임
    private String nickname;
    private String password;

    @Builder
    public Post(String title, String content, Member member,String password) {
        this.title = title;
        this.content = content;
        this.member = member;
        this.password = password;
    }

    public void update(String title, String content){
        this.title = title;
        this.content = content;
    }
}
