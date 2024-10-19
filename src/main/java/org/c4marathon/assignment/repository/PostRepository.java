package org.c4marathon.assignment.repository;

import org.c4marathon.assignment.domain.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    // 처음 조회할 때, 최신 글부터 가져오기 (최신순으로 size만큼 가져오기)
    @Query("SELECT p FROM Post p ORDER BY p.postId DESC")
    List<Post> findTopNPosts(Pageable pageable);

    // 특정 postId 이후의 게시글 가져오기
    @Query("SELECT p FROM Post p WHERE p.postId < :lastPostId ORDER BY p.postId DESC")
    List<Post> findNextPosts(@Param("lastPostId") Long lastPostId, Pageable pageable);
}
