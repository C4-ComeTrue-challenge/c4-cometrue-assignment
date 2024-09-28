package org.c4marathon.assignment.seller.application;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SignUpSellerService {
	public UUID register(Command cmd) {
		return null;
	}

	public record Command(
		String email,
		String password,
		String name,
		String licenseNumber
	) {
	}
}
