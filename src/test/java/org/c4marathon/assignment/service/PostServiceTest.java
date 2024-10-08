package org.c4marathon.assignment.service;

import org.c4marathon.assignment.domain.Member;
import org.c4marathon.assignment.domain.Post;
import org.c4marathon.assignment.domain.request.PostRequest;
import org.c4marathon.assignment.domain.response.PostResponse;
import org.c4marathon.assignment.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PostServiceTest {
    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("게시글 작성")
    void createPostTest() {
        // given
        Member member = Member.builder().email("kumsh0330@naver.com").nickname("sh").build();
        PostRequest postRequest = new PostRequest("제목", "내용");

        // when
        postService.createPost(postRequest, member);

        // then
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    @DisplayName("게시글 전체 조회")
    void getAllPostsTest() {
        // given
        Pageable pageable = PageRequest.of(0, 5);
        Member member = Member.builder().email("kumsh0330@naver.com").nickname("sh").build();
        Post post = Post.builder().title("제목").content("내용").member(member).build();
        Page<Post> postPage = new PageImpl<>(Collections.singletonList(post));
        when(postRepository.findAll(pageable)).thenReturn(postPage);

        // when
        Page<PostResponse> result = postService.getAllPosts(pageable);

        // then
        assertEquals(1, result.getTotalElements());
        assertEquals("제목", result.getContent().get(0).getTitle());
        verify(postRepository, times(1)).findAll(pageable);
    }
}