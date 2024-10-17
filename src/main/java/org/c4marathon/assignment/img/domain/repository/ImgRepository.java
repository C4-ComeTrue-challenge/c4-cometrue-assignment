package org.c4marathon.assignment.img.domain.repository;

import org.c4marathon.assignment.img.domain.Img;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ImgRepository {
	private final ImgJpaRepository imgJpaRepository;

	public void save(String fileName) {
		imgJpaRepository.save(Img.builder().fileName(fileName).build());
	}
}
