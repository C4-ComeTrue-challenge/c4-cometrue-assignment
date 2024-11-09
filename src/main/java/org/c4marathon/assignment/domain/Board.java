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
public class Board extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    @Column(nullable = false)
    private String boardName; // 게시판 이름

    @Column(nullable = false)
    private String description; // 게시판 설명

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private Member createdBy; // 게시판을 만든 유저

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notice> notices = new ArrayList<>();

    @Builder
    public Board(String boardName, String description, Member createdBy) {
        this.boardName = boardName;
        this.description = description;
        this.createdBy = createdBy;
    }

    public void updateMetadata(String name, String description) {
        this.boardName = name;
        this.description = description;
    }

    public boolean isCreatedBy(Member member) {
        return this.createdBy.equals(member);
    }
}
