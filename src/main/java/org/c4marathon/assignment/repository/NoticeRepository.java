package org.c4marathon.assignment.repository;

import org.c4marathon.assignment.domain.Notice;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {
    // 게시판의 공지사항 조회 (Pageable 사용)
    @Query("SELECT n FROM Notice n WHERE n.board.boardId = :boardId ORDER BY n.createdDate DESC")
    List<Notice> findTopNoticesByBoardId(@Param("boardId") Long boardId, Pageable pageable);
}
