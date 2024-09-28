package org.c4marathon.assignment.member.dto.request;

import org.c4marathon.assignment.member.domain.MemberAuthority;

public record CreateMemberRequest(
        String nickname,
        String password,
        MemberAuthority authority
) {
}
