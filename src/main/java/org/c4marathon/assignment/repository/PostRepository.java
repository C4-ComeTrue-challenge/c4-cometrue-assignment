package org.c4marathon.assignment.repository;

import org.c4marathon.assignment.domain.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("""
    SELECT p FROM Post p 
    WHERE p.board.boardId = :boardId 
    AND (:lastPostId IS NULL OR p.postId < :lastPostId) 
    ORDER BY p.postId DESC
""")
    List<Post> findPostsByBoardIdAndLastPostId(
            @Param("boardId") Long boardId,
            @Param("lastPostId") Long lastPostId,
            Pageable pageable);
}
