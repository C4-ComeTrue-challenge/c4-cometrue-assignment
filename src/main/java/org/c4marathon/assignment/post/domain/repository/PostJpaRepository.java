package org.c4marathon.assignment.post.domain.repository;

import org.c4marathon.assignment.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PostJpaRepository extends JpaRepository<Post, Long> {

    // index (created_at DESC, post_id ASC)
    @Query("""
    SELECT p
    FROM Post p
    ORDER BY p.createdAt DESC, p.id ASC
    LIMIT :limit
    """)
    List<Post> findAllPost(@Param("limit") int limit);


    // index (created_at DESC, post_id ASC)
    @Query("""
    SELECT p
    FROM Post p
    WHERE (p.createdAt < :createdAt) OR (p.createdAt = :createdAt AND p.id > :id)
    ORDER BY p.createdAt DESC, p.id ASC
    LIMIT :limit
    """)
    List<Post> findAllPostWithPageToken(
            @Param("createdAt") LocalDateTime createdAt,
            @Param("id") Long id,
            @Param("limit") int limit
    );

}
