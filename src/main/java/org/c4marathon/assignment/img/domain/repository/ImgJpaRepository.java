package org.c4marathon.assignment.img.domain.repository;

import org.c4marathon.assignment.img.domain.Img;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImgJpaRepository extends JpaRepository<Img, Long>, ImgCustomRepository {
	boolean existsByFileName(String fileName);
}
