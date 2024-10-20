package org.c4marathon.assignment.Image.domain.repository;

import org.c4marathon.assignment.Image.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {

    Optional<Image> findByImageUrl(String imageUrl);
}
