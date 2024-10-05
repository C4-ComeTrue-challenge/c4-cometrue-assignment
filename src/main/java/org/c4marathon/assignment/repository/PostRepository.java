package org.c4marathon.assignment.repository;

import org.c4marathon.assignment.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

}
