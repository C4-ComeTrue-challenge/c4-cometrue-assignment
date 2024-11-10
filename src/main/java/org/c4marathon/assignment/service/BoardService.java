package org.c4marathon.assignment.service;

import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.domain.Board;
import org.c4marathon.assignment.domain.Member;
import org.c4marathon.assignment.domain.Notice;
import org.c4marathon.assignment.domain.Post;
import org.c4marathon.assignment.exception.UnauthorizedAccessException;
import org.c4marathon.assignment.repository.BoardRepository;
import org.c4marathon.assignment.repository.NoticeRepository;
import org.c4marathon.assignment.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final NoticeRepository noticeRepository;
    private final PostRepository postRepository;

    @Transactional
    public Board createBoard(String boardName, String description, Member createdBy) {
        Board board = Board.builder()
                .boardName(boardName)
                .description(description)
                .createdBy(createdBy)
                .build();
        return boardRepository.save(board);
    }

    @Transactional
    public void updateBoardMetadata(Long boardId, String name, String description, Member member) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시판이 존재하지 않습니다."));
        if (!board.isCreatedBy(member)) {
            throw new UnauthorizedAccessException("게시판 수정 권한이 없습니다.");
        }
        board.updateMetadata(name, description);
    }

    @Transactional
    public void deletePost(Long boardId, Long postId, Member member) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시판이 존재하지 않습니다."));
        if (!board.isCreatedBy(member)) {
            throw new UnauthorizedAccessException("게시글 삭제 권한이 없습니다.");
        }
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
        postRepository.delete(post);
    }

    @Transactional
    public Notice addNotice(Long boardId, String title, String content, Member member) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시판이 존재하지 않습니다."));
        if (!board.isCreatedBy(member)) {
            throw new UnauthorizedAccessException("공지사항 추가 권한이 없습니다.");
        }
        if (board.getNotices().size() >= 3) {
            throw new IllegalArgumentException("공지사항은 최대 3개까지 등록할 수 있습니다.");
        }
        Notice notice = Notice.builder()
                .title(title)
                .content(content)
                .board(board)
                .build();
        return noticeRepository.save(notice);
    }
}
