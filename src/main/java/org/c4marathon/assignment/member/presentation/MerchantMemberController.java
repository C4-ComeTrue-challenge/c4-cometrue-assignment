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
@RequestMapping("/merchant/members")
@RequiredArgsConstructor
public class MerchantMemberController {

    private final MemberAccountFacadeService memberFacadeService;

    @PostMapping
    private ResponseEntity<Void> createMember(
            @RequestBody CreateMemberRequest request
    ) {
        memberFacadeService.createMerchantMemberAndAccount(request.nickname(), request.password());
        return ResponseEntity.status(CREATED).build();
    }
}
