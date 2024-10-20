package org.c4marathon.assignment.post.presentation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.global.model.PageInfo;
import org.c4marathon.assignment.global.util.SessionConst;
import org.c4marathon.assignment.post.presentation.dto.PostCreateRequest;
import org.c4marathon.assignment.post.presentation.dto.PostDeleteRequest;
import org.c4marathon.assignment.post.presentation.dto.PostResponse;
import org.c4marathon.assignment.post.presentation.dto.PostUpdateRequest;
import org.c4marathon.assignment.post.service.PostService;
import org.c4marathon.assignment.user.domain.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class PostController {
    private final PostService postService;

    @PostMapping("/v1/post/{boardId}")
    public ResponseEntity<PostResponse> createPost(@Valid @RequestBody PostCreateRequest request,
                                                   @PathVariable @Positive Long boardId,
                                                   HttpServletRequest http) {
        /*
         * 401, 403과 같은 인가같은 경우는 나중에 스프링 인터셉터를 통해 처리하도록 하자
         * */
        Long sessionUser = SessionConst.getSessionUser(http);
        PostResponse post;
        if (sessionUser != null) {
            post = postService.createPostByUser(request.toServiceDto(), sessionUser, boardId);
        } else {
            post = postService.createPostByGuest(request.toServiceDto(), boardId);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(post);
    }

    @GetMapping("/v1/post")
    public PageInfo<PostResponse> getAllPosts(
            @RequestParam(required = false) String pageToken,
            @RequestParam @Positive @Max(100) int count
    ) {
        return postService.getAllPosts(pageToken, count);

    }

    @PatchMapping("/v1/post/{postId}")
    public ResponseEntity<Void> updatePost(
            @Valid @RequestBody PostUpdateRequest request,
            @PathVariable @Positive Long postId,
            HttpServletRequest http
    ) {
        Long sessionUser = SessionConst.getSessionUser(http);
        if (sessionUser != null) {
            postService.updatePostByUser(request.toServiceDto(), postId, sessionUser);
        } else {
            postService.updatePostByGuest(request.toServiceDto(), postId);
        }

        return ResponseEntity.noContent().build();

    }

    @DeleteMapping("/v1/post/{postId}")
    public ResponseEntity<Void> deletePost(
            @RequestBody PostDeleteRequest request,
            @PathVariable @Positive Long postId,
            HttpServletRequest http
    ) {
        Long sessionUser = SessionConst.getSessionUser(http);
        if (sessionUser != null) {
            postService.deletePostByUser(request.toServiceDto(), postId, sessionUser);
        } else {
            postService.deletePostByGuest(request.toServiceDto(), postId);
        }

        return ResponseEntity.noContent().build();
    }
}


