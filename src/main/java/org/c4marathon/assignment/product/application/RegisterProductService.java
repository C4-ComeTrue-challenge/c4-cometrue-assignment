package org.c4marathon.assignment.product.application;

import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public class RegisterProductService {

	public Long register(Command cmd) {
		return null;
	}

	public record Command(
		UUID sellerId,
		String name,
		String description,
		Long price,
		Long stock
	) {
	}
}
