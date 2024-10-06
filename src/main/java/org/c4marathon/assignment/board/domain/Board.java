package org.c4marathon.assignment.board.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.c4marathon.assignment.global.BaseTimeEntity;
import org.c4marathon.assignment.user.domain.User;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Board extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @Lob
    @Column(nullable = false)
    private String content;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 20)
    private String writerName;

    @Column
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public boolean isWrittenByUser() {
        return this.user != null;
    }

    public Board(String title,String content, String writerName, String password) {
        this.title=title;
        this.content = content;
        this.writerName = writerName;
        this.password = password;
    }


    public Board(String title, String content, User user) {
        this.title=title;
        this.content = content;
        this.user = user;
        this.writerName = user.getNickname();
    }
}
