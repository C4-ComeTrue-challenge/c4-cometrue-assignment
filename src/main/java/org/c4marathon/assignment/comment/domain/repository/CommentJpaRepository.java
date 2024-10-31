package org.c4marathon.assignment.comment.domain.repository;

import org.c4marathon.assignment.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentJpaRepository extends JpaRepository<Comment, Long>, CommentCustomRepository {
}
