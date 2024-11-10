package org.c4marathon.assignment.product.service;

import org.assertj.core.api.Assertions;
import org.c4marathon.assignment.account.domain.repository.AccountRepository;
import org.c4marathon.assignment.member.domain.Member;
import org.c4marathon.assignment.member.domain.Merchant;
import org.c4marathon.assignment.member.domain.repository.CustomerRepository;
import org.c4marathon.assignment.member.domain.repository.MemberRepository;
import org.c4marathon.assignment.member.domain.repository.MerchantRepository;
import org.c4marathon.assignment.member.service.MemberAccountFacadeService;
import org.c4marathon.assignment.product.domain.Product;
import org.c4marathon.assignment.product.domain.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MerchantProductServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private MerchantRepository merchantRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MerchantProductService merchantProductService;

    @Autowired
    private MemberAccountFacadeService memberAccountFacadeService;

    @AfterEach
    void tearDown() {
        memberRepository.deleteAllInBatch();
        customerRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        merchantRepository.deleteAllInBatch();
        accountRepository.deleteAllInBatch();
    }

    @DisplayName("판매자는 상품을 등록할 수 있다.")
    @Test
    void 상품_등록_테스트() {
        String username = "merchant";
        String password = "password";

        memberAccountFacadeService.createMerchantMemberAndAccount(username, password);
        Member findMember = memberRepository.findByNickname("merchant").get();
        Merchant findMerchant = merchantRepository.findByMemberId(findMember.getId()).get();

        merchantProductService.addProduct(
                findMerchant.getId(),
                "컴퓨터",
                "개쩌는 컴퓨터",
                10000L,
                10L);

        Product computer = productRepository.findByMerchantIdAndProductName(findMerchant.getId(), "컴퓨터").get();
        Merchant productMerchant = computer.getMerchant();
        assertThat(computer).extracting("productName", "description", "price", "stock.stock")
                            .containsExactly("컴퓨터", "개쩌는 컴퓨터", 10000L, 10L);
        assertThat(productMerchant.getId()).isEqualTo(findMerchant.getId());
    }
}