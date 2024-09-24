package org.c4marathon.assignment.user.application;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SignUpService {
	public UUID registerUser(Command command) {
		return null;
	}

	public record Command(
		String email,
		String password,
		String name,
		String type
	) {
	}
}
