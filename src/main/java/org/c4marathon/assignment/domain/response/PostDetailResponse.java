package org.c4marathon.assignment.domain.response;

import lombok.Data;
import lombok.Getter;
import org.c4marathon.assignment.domain.Post;

@Data
@Getter
public class PostDetailResponse {
    private Long postId;
    private String title;
    private String content;
    private String nickname;

    public PostDetailResponse(Post post) {
        this.postId = post.getPostId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.nickname = post.getMember().getNickname();
    }
}
