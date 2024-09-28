package org.c4marathon.assignment.seller.domain;

import java.util.Optional;
import java.util.UUID;

public interface SellerRepository {
	Seller save(Seller seller);

	Optional<Seller> findById(UUID id);
}
