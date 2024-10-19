package org.c4marathon.assignment.seller.application;

import java.util.UUID;

import org.c4marathon.assignment.common.encoder.PasswordEncoder;
import org.c4marathon.assignment.seller.domain.Seller;
import org.c4marathon.assignment.seller.domain.SellerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SignUpSellerService {
	private final SellerRepository sellerRepository;
	private final PasswordEncoder passwordEncoder;

	public SignUpSellerService(SellerRepository sellerRepository, PasswordEncoder passwordEncoder) {
		this.sellerRepository = sellerRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public UUID register(Command cmd) {
		Seller newSeller = sellerRepository.save(
			new Seller(cmd.email(), cmd.password(), cmd.name(), cmd.licenseNumber(), passwordEncoder));
		return newSeller.getId();
	}

	public record Command(
		String email,
		String password,
		String name,
		String licenseNumber
	) {
	}
}
