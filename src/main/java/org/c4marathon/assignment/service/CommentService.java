package org.c4marathon.assignment.service;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.c4marathon.assignment.domain.Comment;
import org.c4marathon.assignment.domain.Member;
import org.c4marathon.assignment.domain.Post;
import org.c4marathon.assignment.domain.request.CommentRequest;
import org.c4marathon.assignment.domain.response.CommentResponse;
import org.c4marathon.assignment.exception.CommentNotFoundException;
import org.c4marathon.assignment.exception.PostNotFoundException;
import org.c4marathon.assignment.exception.UnauthorizedException;
import org.c4marathon.assignment.repository.CommentRepository;
import org.c4marathon.assignment.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Transactional(readOnly = true)
    public Page<CommentResponse> getComments(Long postId, Pageable pageable) {
        // 댓글/답글 구조를 깊이 우선 탐색하여 조회 및 페이징 처리
        Page<Comment> comments = commentRepository.findByPostId(postId, pageable);
        return comments.map(CommentResponse::new);
    }

    public CommentResponse createComment(Long postId, CommentRequest commentRequest, HttpSession session) {
        Member member = (Member) session.getAttribute("member");
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("게시글이 존재하지 않습니다."));
        Comment parent=null;
        // 부모 댓글 찾기
        if(commentRequest.getParentId() != null){
            parent = commentRepository.findById(commentRequest.getParentId())
                    .orElseThrow(() -> new CommentNotFoundException("해당 댓글이 존재하지 않습니다."));
        }


        Comment comment = Comment.builder()
                .content(commentRequest.getContent())
                .member(member)
                .post(post)
                .parent(parent)
                .nickname(member==null? commentRequest.getNickname(): member.getNickname())
                .password(commentRequest.getPassword())
                .build();

        commentRepository.save(comment);
        return new CommentResponse(comment);
    }

    @Transactional
    public CommentResponse updateComment(Long commentId, CommentRequest request, HttpSession session) {
        Member member = (Member) session.getAttribute("member");
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("해당 댓글이 존재하지 않습니다."));

        // 권한 확인: 회원 또는 비회원 닉네임+비밀번호 검증
        if (member != null && !comment.isWrittenBy(member)) {
            throw new UnauthorizedException("댓글을 수정할 권한이 없습니다.");
        } else if (member == null && !comment.isWrittenByGuest(request.getNickname(), request.getPassword())) {
            throw new UnauthorizedException("댓글을 수정할 권한이 없습니다.");
        }

        comment.update(request.getContent());
        return new CommentResponse(comment);
    }

    @Transactional
    public void deleteComment(Long commentId, CommentRequest request, HttpSession session) {
        Member member = (Member) session.getAttribute("member");
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("해당 댓글이 존재하지 않습니다."));

        // 권한 확인: 회원 또는 비회원 닉네임+비밀번호 검증
        if (member != null && !comment.isWrittenBy(member)) {
            throw new UnauthorizedException("댓글을 삭제할 권한이 없습니다.");
        } else if (member == null && !comment.isWrittenByGuest(request.getNickname(), request.getPassword())) {
            throw new UnauthorizedException("댓글을 삭제할 권한이 없습니다.");
        }

        comment.delete(); // 소프트 삭제
    }
}
