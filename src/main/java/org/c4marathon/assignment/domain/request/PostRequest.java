package org.c4marathon.assignment.domain.request;

import lombok.Data;

@Data
public class PostRequest {
    private String title;
    private String content;
    private String password;

    public PostRequest(String title, String content,String password) {
        this.title = title;
        this.content = content;
        this.password=password;
    }
}
