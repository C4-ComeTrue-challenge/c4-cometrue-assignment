package org.c4marathon.assignment.seller.infra.persist;

import java.util.Optional;
import java.util.UUID;

import org.c4marathon.assignment.seller.domain.Seller;
import org.c4marathon.assignment.seller.domain.SellerRepository;
import org.c4marathon.assignment.seller.infra.persist.datajpa.JpaSellerRecordRepository;
import org.springframework.stereotype.Repository;

@Repository
public class JpaSellerRepository implements SellerRepository {
	private final JpaSellerRecordRepository repository;

	public JpaSellerRepository(JpaSellerRecordRepository jpaSellerRecordRepository) {
		this.repository = jpaSellerRecordRepository;
	}

	@Override
	public Seller save(Seller seller) {
		return repository.save(seller);
	}

	@Override
	public Optional<Seller> findById(UUID id) {
		return repository.findById(id);
	}
}
