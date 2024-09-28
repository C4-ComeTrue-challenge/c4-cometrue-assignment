package org.c4marathon.assignment.member.service;

import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.global.exception.AuthException;
import org.c4marathon.assignment.member.domain.Merchant;
import org.c4marathon.assignment.member.domain.repository.MerchantRepository;
import org.springframework.stereotype.Service;

import static org.c4marathon.assignment.global.exception.exceptioncode.ExceptionCode.MEMBER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class MerchantService {

    private final MerchantRepository merchantRepository;

    public Merchant findMerchantById(Long id) {
        return merchantRepository.findById(id)
                                 .orElseThrow(() -> new AuthException(MEMBER_NOT_FOUND));
    }
}
