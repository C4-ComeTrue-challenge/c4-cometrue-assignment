package org.c4marathon.assignment.member.presentation;

import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.member.domain.MemberAuthority;
import org.c4marathon.assignment.member.dto.request.CreateMemberRequest;
import org.c4marathon.assignment.member.service.MemberFacadeService;
import org.c4marathon.assignment.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberFacadeService memberFacadeService;

    @PostMapping
    private ResponseEntity<Void> createMember(
            @RequestBody CreateMemberRequest request
    ) {
        memberFacadeService.createMemberAndAccount(request.nickname(), request.password(), request.authority());
        return ResponseEntity.status(CREATED).build();
    }
}
