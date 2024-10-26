package org.c4marathon.assignment.service;

import jakarta.servlet.http.HttpSession;
import org.c4marathon.assignment.domain.Comment;
import org.c4marathon.assignment.domain.Member;
import org.c4marathon.assignment.domain.Post;
import org.c4marathon.assignment.domain.request.CommentRequest;
import org.c4marathon.assignment.domain.response.CommentResponse;
import org.c4marathon.assignment.repository.CommentRepository;
import org.c4marathon.assignment.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @MockBean
    private PostRepository postRepository;

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private HttpSession session;

    @Test
    @DisplayName("회원 댓글 쓰기 성공")
    void createCommentAsMember() {
        // Given
        Long postId = 1L;
        Member member = new Member("abc123@naver.com","1234","닉네임1");
        Post post = new Post("제목","내용",member,null);
        CommentRequest request = new CommentRequest("댓글", null, null, null);

        when(session.getAttribute("member")).thenReturn(member);
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        // When
        CommentResponse response = commentService.createComment(postId, request, session);

        // Then
        assertNotNull(response);
        assertEquals("댓글", response.getContent());
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    @DisplayName("비회원 댓글 쓰기 성공")
    void createCommentAsGuest() {
        // Given
        Long postId = 1L;
        Post post = new Post("제목","내용",null,"1234");
        CommentRequest request = new CommentRequest("댓글", null, "게스트", "1234");

        when(session.getAttribute("member")).thenReturn(null);
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        // When
        CommentResponse response = commentService.createComment(postId, request, session);

        // Then
        assertNotNull(response);
        assertEquals("댓글", response.getContent());
        assertEquals("게스트", response.getNickname());
        verify(commentRepository).save(any(Comment.class));
    }
}