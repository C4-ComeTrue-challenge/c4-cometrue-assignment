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

import java.util.List;

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

    // 본문 중간에 이미지 URL을 삽입하는 메서드
    private String insertImagesIntoContent(String content, List<String> imageUrls) {
        if (imageUrls == null || imageUrls.isEmpty()) {
            return content;
        }

        // 예시: 각 이미지 URL을 본문 끝에 삽입 (또는 적절한 위치를 지정 가능)
        StringBuilder contentWithImages = new StringBuilder(content);
        for (String imageUrl : imageUrls) {
            contentWithImages.append("<br><img src=\"")
                    .append(imageUrl)
                    .append("\" alt=\"Image\"><br>");
        }

        return contentWithImages.toString();
    }

    // 게시글 전체 조회
    public Page<PostResponse> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable).map(PostResponse::new);
    }
}
