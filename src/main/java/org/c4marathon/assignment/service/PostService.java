package org.c4marathon.assignment.service;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.domain.Member;
import org.c4marathon.assignment.domain.Post;
import org.c4marathon.assignment.domain.request.PostRequest;
import org.c4marathon.assignment.domain.response.PostResponse;
import org.c4marathon.assignment.exception.PasswordNotFoundException;
import org.c4marathon.assignment.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    // 게시글 작성
    @Transactional
    public void createPost(PostRequest postRequest, HttpSession session) {
        Member member = (Member) session.getAttribute("member");
        String password=postRequest.getPassword();
        if(member==null && password==null) {
            throw new PasswordNotFoundException("비회원은 비밀번호를 입력해주세요");
        }
        Post post = Post.builder()
                .title(postRequest.getTitle())
                .content(postRequest.getContent())
                .member(member)  // 로그인한 회원과 연관관계 설정
                .password(password) // 비로그인 회원의 비밀번호 설정
                .build();
        postRepository.save(post);
    }

    // 게시글 전체 조회
    public Page<PostResponse> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable).map(PostResponse::new);
    }
}
