package org.c4marathon.assignment.seller.domain;

import java.util.UUID;

import org.c4marathon.assignment.common.entity.Point;

/**
 * 테스트를 위한 CustomerFactory
 * 프로덕션 코드로 승격 금지
 */
public abstract class TestSellerFactory {
	public static Seller create(UUID id, String email, String encodedPassword, String name, String licenseNumber,
								Point point) {
		return new Seller(id, email, encodedPassword, name, licenseNumber, point);
	}
}
