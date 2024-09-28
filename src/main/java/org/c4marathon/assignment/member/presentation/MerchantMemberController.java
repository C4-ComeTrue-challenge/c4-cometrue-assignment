package org.c4marathon.assignment.member.presentation;

import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.member.dto.request.CreateMemberRequest;
import org.c4marathon.assignment.member.service.MemberFacadeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/merchant/members")
@RequiredArgsConstructor
public class MerchantMemberController {

    private final MemberFacadeService memberFacadeService;

    @PostMapping
    private ResponseEntity<Void> createMember(
            @RequestBody CreateMemberRequest request
    ) {
        memberFacadeService.createMerchantMemberAndAccount(request.nickname(), request.password());
        return ResponseEntity.status(CREATED).build();
    }
}
