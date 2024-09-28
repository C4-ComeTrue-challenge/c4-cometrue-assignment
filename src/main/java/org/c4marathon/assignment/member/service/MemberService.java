package org.c4marathon.assignment.member.service;

import lombok.RequiredArgsConstructor;

import org.c4marathon.assignment.member.domain.Customer;
import org.c4marathon.assignment.member.domain.Member;
import org.c4marathon.assignment.member.domain.Merchant;
import org.c4marathon.assignment.member.domain.repository.CustomerRepository;
import org.c4marathon.assignment.member.domain.repository.MemberRepository;
import org.c4marathon.assignment.member.domain.repository.MerchantRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MerchantRepository merchantRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    protected Long registerMerchantUser(String nickname, String password) {
        Member member = memberRepository.save(Member.merchant(nickname, passwordEncoder.encode(password)));
        Merchant merchant = merchantRepository.save(Merchant.of(member.getId(), nickname));
        member.addMerchant(merchant.getId());
        return merchant.getId();
    }

    @Transactional
    protected Long registerCustomerUser(String nickname, String password) {
        Member member = memberRepository.save(Member.customer(nickname, passwordEncoder.encode(password)));
        Customer customer = customerRepository.save(Customer.of(member.getId(), nickname));
        member.addCustomer(customer.getId());
        return customer.getId();
    }

}