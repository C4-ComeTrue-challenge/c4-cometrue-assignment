package org.c4marathon.assignment.member.service;

import lombok.RequiredArgsConstructor;

import org.c4marathon.assignment.member.domain.Customer;
import org.c4marathon.assignment.member.domain.Member;
import org.c4marathon.assignment.member.domain.Merchant;
import org.c4marathon.assignment.member.domain.MemberAuthority;
import static org.c4marathon.assignment.member.domain.MemberAuthority.CUSTOMER;
import static org.c4marathon.assignment.member.domain.MemberAuthority.MERCHANT;
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
    public Long registerUser(String nickname, String password, MemberAuthority memberAuthority) {
        if (memberAuthority.equals(MERCHANT)) {
            return registerMerchantUser(nickname, password);
        } else {
            return registerCustomerUser(nickname, password);
        }
    }

    @Transactional
    protected Long registerMerchantUser(String nickname, String password) {
        Merchant merchant = merchantRepository.save(Merchant.of(nickname));
        Member merchantMember = Member.merchant(merchant.getId(), nickname, passwordEncoder.encode(password));
        memberRepository.save(merchantMember);

        return merchantMember.getId();
    }

    @Transactional
    protected Long registerCustomerUser(String nickname, String password) {
        Customer customer = customerRepository.save(Customer.of(nickname));
        Member customerMember = Member.customer(customer.getId(), nickname, passwordEncoder.encode(password));
        memberRepository.save(customerMember);

        return customerMember.getId();
    }

}