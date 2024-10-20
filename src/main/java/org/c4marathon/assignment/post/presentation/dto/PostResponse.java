package org.c4marathon.assignment.post.presentation.dto;

import lombok.Builder;
import org.c4marathon.assignment.post.domain.Post;
import org.c4marathon.assignment.post.domain.PostType;

import java.time.LocalDateTime;

@Builder
public record PostResponse(
        Long boardId,
        Long postId,
        PostType postType,
        String title,
        String content,
        String guestName,
        String guestPassword,
        LocalDateTime createdAt
) {

    public static PostResponse of(Long boardId, Post post) {
        return PostResponse.builder()
                .boardId(boardId)
                .postId(post.getId())
                .postType(post.getPostType())
                .title(post.getTitle())
                .content(post.getContent())
                .guestName(post.getGuestName())
                .guestPassword(post.getGuestPassword())
                .createdAt(post.getCreatedAt())
                .build();
    }

    public static PostResponse of(Post post) {
        return PostResponse.builder()
                .postId(post.getId())
                .postType(post.getPostType())
                .title(post.getTitle())
                .content(post.getContent())
                .guestName(post.getGuestName())
                .guestPassword(post.getGuestPassword())
                .createdAt(post.getCreatedAt())
                .build();
    }

}
