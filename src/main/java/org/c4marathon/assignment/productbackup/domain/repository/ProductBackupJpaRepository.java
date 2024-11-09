package org.c4marathon.assignment.productbackup.domain.repository;


import org.c4marathon.assignment.productbackup.domain.ProductBackup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductBackupJpaRepository extends JpaRepository<ProductBackup, Long> {
}
