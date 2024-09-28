package org.c4marathon.assignment.seller.infra.persist.datajpa;

import java.util.UUID;

import org.c4marathon.assignment.seller.domain.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaSellerRecordRepository extends JpaRepository<Seller, UUID> {
}
