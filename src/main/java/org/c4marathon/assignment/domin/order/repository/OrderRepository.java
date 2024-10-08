package org.c4marathon.assignment.domin.order.repository;

import org.c4marathon.assignment.domin.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}
