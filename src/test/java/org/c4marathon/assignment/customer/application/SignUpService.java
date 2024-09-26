package org.c4marathon.assignment.customer.application;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SignUpService {
	public UUID register(Command cmd) {
		return null;
	}

	public record Command(
		String email,
		String password,
		String name
	) {
	}
}
