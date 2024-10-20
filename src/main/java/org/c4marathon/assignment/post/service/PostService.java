package org.c4marathon.assignment.post.service;

import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.global.model.PageInfo;
import org.c4marathon.assignment.post.domain.repository.PostRepository;
import org.c4marathon.assignment.post.presentation.dto.PostResponse;
import org.c4marathon.assignment.post.service.dto.PostCreateServiceRequest;
import org.c4marathon.assignment.post.service.dto.PostDeleteServiceRequest;
import org.c4marathon.assignment.post.service.dto.PostUpdateServiceRequest;
import org.c4marathon.assignment.user.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;

    public PostResponse createPostByUser(PostCreateServiceRequest request, Long user, Long boardId) {

        return postRepository.createPostByUser(request, user, boardId);
    }

    public PostResponse createPostByGuest(PostCreateServiceRequest request, Long boardId) {
        return postRepository.createPostByGuest(request, boardId);
    }

    @Transactional(readOnly = true)
    public PageInfo<PostResponse> getAllPosts(String pageToken, int count) {
        if (pageToken == null) {
            return postRepository.findAllPostWithoutPageToken(count + 1);
        } else {
            return postRepository.findAllPostWithPageToken(pageToken, count + 1);
        }
    }

    @Transactional(readOnly = true)
    public PageInfo<PostResponse> getPostByBoardId(Long boardId, String pageToken, int count) {
        if (pageToken == null) {
            return postRepository.findPostByBoardIdWithoutPageToken(boardId, count + 1);
        } else {
            return postRepository.findPostByBoardIdWithPageToken(boardId, pageToken, count + 1);
        }
    }

    @Transactional
    public void updatePostByUser(PostUpdateServiceRequest request, Long postId, Long userId) {
        postRepository.updatePostByUser(request, postId, userId);
    }

    @Transactional
    public void updatePostByGuest(PostUpdateServiceRequest request, Long postId) {
        postRepository.updatePostByGuest(request, postId);
    }

    @Transactional
    public void deletePostByUser(PostDeleteServiceRequest request, Long postId, Long userId) {
        postRepository.deletePostByUser(request, postId, userId);
    }

    @Transactional
    public void deletePostByGuest(PostDeleteServiceRequest request, Long postId) {
        postRepository.deletePostByGuest(request, postId);
    }
}
