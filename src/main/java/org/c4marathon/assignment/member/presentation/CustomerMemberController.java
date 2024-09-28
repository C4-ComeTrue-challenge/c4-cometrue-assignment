package org.c4marathon.assignment.member.presentation;

import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.member.dto.request.CreateMemberRequest;
import org.c4marathon.assignment.member.service.MemberFacadeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.c4marathon.assignment.member.domain.MemberAuthority.CUSTOMER;
import static org.c4marathon.assignment.member.domain.MemberAuthority.MERCHANT;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class CustomerMemberController {

    private final MemberFacadeService memberFacadeService;

    @PostMapping
    private ResponseEntity<Void> createMember(
            @RequestBody CreateMemberRequest request
    ) {
        memberFacadeService.createCustomerMemberAndAccount(request.nickname(), request.password(), CUSTOMER);
        return ResponseEntity.status(CREATED).build();
    }
}
