package org.c4marathon.assignment.domain.response;

import lombok.Data;
import org.c4marathon.assignment.domain.Post;

@Data
public class PostDetailResponse {
    private String title;
    private String content;
    private String nickname;

    public PostDetailResponse(Post post) {
        this.title = post.getTitle();
        this.content = post.getContent();
        this.nickname = post.getMember().getNickname();
    }
}
