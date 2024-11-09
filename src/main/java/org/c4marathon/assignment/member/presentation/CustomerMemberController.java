package org.c4marathon.assignment.member.presentation;

import static org.springframework.http.HttpStatus.CREATED;

import org.c4marathon.assignment.member.dto.request.CreateMemberRequest;
import org.c4marathon.assignment.member.service.MemberAccountFacadeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class CustomerMemberController {

    private final MemberAccountFacadeService memberFacadeService;

    @PostMapping
    private ResponseEntity<Void> createMember(
            @RequestBody CreateMemberRequest request
    ) {
        memberFacadeService.createCustomerMemberAndAccount(request.nickname(), request.password());
        return ResponseEntity.status(CREATED).build();
    }
}
