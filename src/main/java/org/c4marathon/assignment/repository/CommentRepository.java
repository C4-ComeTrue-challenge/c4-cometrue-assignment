package org.c4marathon.assignment.repository;

import org.c4marathon.assignment.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT c FROM Comment c WHERE c.post.postId = :postId ORDER BY COALESCE(c.parent.commentId, c.commentId), c.parent.commentId ASC, c.createdDate ASC")
    Page<Comment> findCommentsWithReplies(@Param("postId") Long postId, Pageable pageable);
}
