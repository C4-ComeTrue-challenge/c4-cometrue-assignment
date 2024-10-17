package org.c4marathon.assignment.img.domain.repository;

import java.util.List;

import org.c4marathon.assignment.img.domain.Img;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImgJpaRepository extends JpaRepository<Img, Long> {

	List<Img> findByFileNameIn(List<String> fileNames);

	boolean existsByFileName(String fileName);
}
