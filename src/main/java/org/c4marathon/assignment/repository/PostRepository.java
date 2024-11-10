package org.c4marathon.assignment.repository;

import org.c4marathon.assignment.domain.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    // 게시판의 일반 게시글 최신순 조회
    @Query("SELECT p FROM Post p WHERE p.board.boardId = :boardId ORDER BY p.postId DESC")
    List<Post> findTopPostsByBoardId(@Param("boardId") Long boardId, Pageable pageable);

    // 특정 postId 이후의 일반 게시글 조회
    @Query("SELECT p FROM Post p WHERE p.board.boardId = :boardId AND p.postId < :lastPostId ORDER BY p.postId DESC")
    List<Post> findNextPostsByBoardId(@Param("boardId") Long boardId, @Param("lastPostId") Long lastPostId, Pageable pageable);
}
