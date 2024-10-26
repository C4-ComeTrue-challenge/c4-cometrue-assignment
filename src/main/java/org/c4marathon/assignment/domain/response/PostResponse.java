package org.c4marathon.assignment.domain.response;

import lombok.Data;
import lombok.Getter;
import org.c4marathon.assignment.domain.Post;

@Data
@Getter
public class PostResponse {
    private Long postId;
    private String title;
    private String nickname;

    public PostResponse(Post post) {
        this.postId = post.getPostId();
        this.title = post.getTitle();
        this.nickname = (post.getMember() != null) ? post.getMember().getNickname() : post.getNickname();
    }
}
