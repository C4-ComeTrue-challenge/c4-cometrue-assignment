package org.c4marathon.assignment.global.config;

import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class UUIDProvider {
	public UUID randomUUID() {
		return UUID.randomUUID();
	}
}
