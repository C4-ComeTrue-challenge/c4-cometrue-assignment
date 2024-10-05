package org.c4marathon.assignment.domain.response;

import lombok.Data;

@Data
public class PostDetailResponse {
    private String title;
    private String content;
    private String nickname;

    public PostDetailResponse(String title, String content, String nickname) {
        this.title = title;
        this.content = content;
        this.nickname = nickname;
    }


}
