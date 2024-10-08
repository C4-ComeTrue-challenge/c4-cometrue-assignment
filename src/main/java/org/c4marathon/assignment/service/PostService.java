package org.c4marathon.assignment.service;

import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.domain.Member;
import org.c4marathon.assignment.domain.Post;
import org.c4marathon.assignment.domain.request.PostRequest;
import org.c4marathon.assignment.domain.response.PostResponse;
import org.c4marathon.assignment.repository.MemberRepository;
import org.c4marathon.assignment.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    // 게시글 작성
    public void createPost(PostRequest postRequest, Member member) {
        Post post = Post.builder()
                .title(postRequest.getTitle())
                .content(postRequest.getContent())
                .member(member)  // 회원과 연관관계 설정
                .build();
        postRepository.save(post);
    }

    // 게시글 전체 조회 (PostResponse로 변환)
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll().stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }
}
