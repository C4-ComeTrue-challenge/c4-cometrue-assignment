package org.c4marathon.assignment.domain.response;

import lombok.Data;

@Data
public class PostResponse {
    private String title;
    private String nickname;

    public PostResponse(String title, String nickname) {
        this.title = title;
        this.nickname = nickname;
    }


}
