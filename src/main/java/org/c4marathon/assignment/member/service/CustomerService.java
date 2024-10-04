package org.c4marathon.assignment.member.service;

import static org.c4marathon.assignment.global.exception.exceptioncode.ExceptionCode.MEMBER_NOT_FOUND;

import org.c4marathon.assignment.global.exception.AuthException;
import org.c4marathon.assignment.member.domain.Customer;
import org.c4marathon.assignment.member.domain.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public Customer findCustomerBy(Long customerId) {
        return customerRepository.findById(customerId)
                                 .orElseThrow(() -> new AuthException(MEMBER_NOT_FOUND));
    }
}
