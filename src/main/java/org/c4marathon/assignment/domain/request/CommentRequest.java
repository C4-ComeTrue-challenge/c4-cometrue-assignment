package org.c4marathon.assignment.domain.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentRequest {
    private String content;
    private Long parentId; // 부모 댓글 ID (답글인 경우)
    private String nickname;
    private String password;
}
