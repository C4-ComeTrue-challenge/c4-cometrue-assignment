package org.c4marathon.assignment.repository;

import org.c4marathon.assignment.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    // 부모 댓글이 없는 경우가 최상위 댓글
    @Query("SELECT c FROM Comment c WHERE c.post.postId = :postId AND c.parent IS NULL ORDER BY c.createdDate DESC")
    Page<Comment> findByPostId(@Param("postId") Long postId, Pageable pageable);
}
