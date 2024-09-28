package org.c4marathon.assignment.member.dto.request;

public record CreateMemberRequest(
        String nickname,
        String password
) {
}
