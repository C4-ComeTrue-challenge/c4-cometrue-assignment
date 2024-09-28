package org.c4marathon.assignment.customer.domain;

import java.util.UUID;

import org.c4marathon.assignment.common.entity.Point;

/**
 * 테스트를 위한 CustomerFactory
 * 프로덕션 코드로 승격 금지
 */
public abstract class TestCustomerFactory {
	public static Customer create(UUID id, String email, String encodedPassword, String name, Point point) {
		return new Customer(id, email, encodedPassword, name, point);
	}
}
