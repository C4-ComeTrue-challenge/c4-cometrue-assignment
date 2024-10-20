package org.c4marathon.assignment.domain.request;

import lombok.Data;

import java.util.List;

@Data
public class PostRequest {
    private String title;
    private String content;
    private String password;
    private List<String> imageUrls;

    public PostRequest(String title, String content,String password) {
        this.title = title;
        this.content = content;
        this.password=password;
    }
}
