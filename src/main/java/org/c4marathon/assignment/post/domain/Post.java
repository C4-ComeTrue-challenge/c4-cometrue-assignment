package org.c4marathon.assignment.post.domain;

import jakarta.persistence.*;
import lombok.*;
import org.c4marathon.assignment.board.domain.Board;
import org.c4marathon.assignment.global.BaseEntity;
import org.c4marathon.assignment.user.domain.User;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Entity
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private PostType postType;

    private String guestName;

    private String guestPassword;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Builder
    private Post(String title, String content, String guestName, String guestPassword, User user, Board board) {
        this.title = title;
        this.content = content;
        this.guestName = guestName;
        this.guestPassword = guestPassword;
        this.user = user;
        this.board = board;
    }

    public static Post createByUser(String title, String content, User user, Board board) {
        Post post = Post.builder()
                .title(title)
                .content(content)
                .user(user)
                .build();
        post.setBoard(board);
        post.setPostTypeUser();

        return post;
    }

    public static Post createByGuest(String title, String content, String guestName, String guestPassword, Board board) {
        Post post = Post.builder()
                .title(title)
                .content(content)
                .guestName(guestName)
                .guestPassword(guestPassword)
                .build();

        post.setBoard(board);
        post.setPostTypeGuest();

        return post;
    }

    public void updatePost(String title, String content) {
        this.title = title;
        this.content = content;
    }


    public void setPostTypeUser() {
        this.postType = PostType.USER;
    }

    public void setPostTypeGuest() {
        this.postType = PostType.GUEST;
    }

    public void setBoard(Board board) {
        this.board = board;
        if (!board.getPosts().contains(this)) {
            board.getPosts().add(this);
        }
    }

}
