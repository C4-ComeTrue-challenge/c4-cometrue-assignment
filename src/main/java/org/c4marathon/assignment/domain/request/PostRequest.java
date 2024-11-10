package org.c4marathon.assignment.domain.request;

import lombok.Data;

import java.util.List;

@Data
public class PostRequest {
    private String title;
    private String content;
    private String password;
    private List<String> imageUrls;
    private Long boardId; // 게시판 ID

    public PostRequest(String title, String content, String password,Long boardId) {
        this.title = title;
        this.content = content;
        this.password = password;
        this.boardId = boardId;
    }
}
