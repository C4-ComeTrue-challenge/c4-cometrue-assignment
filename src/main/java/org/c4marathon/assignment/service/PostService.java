package org.c4marathon.assignment.service;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.c4marathon.assignment.domain.Member;
import org.c4marathon.assignment.domain.Post;
import org.c4marathon.assignment.domain.request.PostRequest;
import org.c4marathon.assignment.domain.response.PostResponse;
import org.c4marathon.assignment.exception.PasswordNotFoundException;
import org.c4marathon.assignment.exception.PostNotFoundException;
import org.c4marathon.assignment.exception.UnauthorizedException;
import org.c4marathon.assignment.repository.PostRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {
    private final PostRepository postRepository;

    // 게시글 작성
    @Transactional
    public void createPost(PostRequest postRequest, HttpSession session) {
        Member member = (Member) session.getAttribute("member");
        String password = postRequest.getPassword();
        if (member == null && password == null) {
            throw new PasswordNotFoundException("비회원은 비밀번호를 입력해주세요");
        }

        String contentWithImages = insertImagesIntoContent(postRequest.getContent(), postRequest.getImageUrls());

        Post post = Post.builder()
                .title(postRequest.getTitle())
                .content(contentWithImages)
                .member(member)  // 로그인한 회원과 연관관계 설정
                .password(password) // 비로그인 회원의 비밀번호 설정
                .build();
        postRepository.save(post);
    }

    // No-Offset 방식으로 게시글 전체 조회
    public List<PostResponse> getAllPosts(Long lastPostId, int size) {
        List<Post> posts;
        Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "postId"));

        if (lastPostId == null) {
            // 첫 페이지일 경우, 최신 게시글부터 가져오기
            posts = postRepository.findTopNPosts(pageable);
        } else {
            // lastPostId 이후의 게시글 가져오기
            posts = postRepository.findNextPosts(lastPostId, pageable);
        }
        return posts.stream().map(PostResponse::new).collect(Collectors.toList());
    }

    // 게시글 단건 조회
    public PostResponse getPostById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("해당 게시글을 찾을 수 없습니다."));
        return new PostResponse(post);
    }

    // 게시글 수정
    @Transactional
    public void updatePost(Long postId, PostRequest postRequest, HttpSession session) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("게시글을 찾을 수 없습니다."));
        Member member = (Member) session.getAttribute("member");
        log.info("{}", member.getMemberId());
        // 권한 확인
        if (isAuthorizedToModifyPost(post, member, postRequest.getPassword())) {
            throw new UnauthorizedException("수정 권한이 없습니다.");
        }

        // 수정된 내용 반영
        post.update(postRequest.getTitle(), postRequest.getContent());
    }

    // 게시글 삭제
    @Transactional
    public void deletePost(Long postId, HttpSession session, String password) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("게시글을 찾을 수 없습니다."));
        Member member = (Member) session.getAttribute("member");

        // 권한 확인
        if (isAuthorizedToModifyPost(post, member, password)) {
            throw new UnauthorizedException("삭제 권한이 없습니다.");
        }

        postRepository.delete(post);
    }

    // 게시글 권한 체크
    private boolean isAuthorizedToModifyPost(Post post, Member member, String password) {
        if (member != null) {
            return post.getMember() == null || !post.getMember().equals(member);
        } else {
            return post.getMember() != null || !post.getPassword().equals(password);
        }
    }

    // 본문 중간에 이미지 URL을 삽입하는 메서드
    private String insertImagesIntoContent(String content, List<String> imageUrls) {
        if (imageUrls == null || imageUrls.isEmpty()) {
            return content;
        }

        // 예시: 각 이미지 URL을 본문 끝에 삽입
        // 다른 위치 지정은 어떻게??
        StringBuilder contentWithImages = new StringBuilder(content);
        for (String imageUrl : imageUrls) {
            contentWithImages.append("<br><img src=\"")
                    .append(imageUrl)
                    .append("\" alt=\"Image\"><br>");
        }

        return contentWithImages.toString();
    }
}
