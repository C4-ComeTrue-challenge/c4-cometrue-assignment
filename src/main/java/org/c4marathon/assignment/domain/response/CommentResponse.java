package org.c4marathon.assignment.domain.response;

import org.c4marathon.assignment.domain.Comment;

import java.util.List;
import java.util.stream.Collectors;

public class CommentResponse {
    private Long commentId;
    private String content;
    private String nickname;
    private List<CommentResponse> replies;

    public CommentResponse(Comment comment) {
        this.commentId = comment.getCommentId();
        this.content = comment.getContent();
        this.nickname = comment.getMember() != null ? comment.getMember().getNickname() : "비회원";
        this.replies = comment.getReplies().stream().map(CommentResponse::new).collect(Collectors.toList());
    }
}
