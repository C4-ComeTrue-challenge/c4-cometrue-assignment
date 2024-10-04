package org.c4marathon.assignment.order.domain.repository;

import java.util.Optional;

import org.c4marathon.assignment.order.domain.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Orders, String> {

    Optional<Orders> findByOrderId(String orderId);
}
