package org.c4marathon.assignment.post.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PostType {
    GUEST("비회원"),
    USER("회원");

    private final String text;
}
