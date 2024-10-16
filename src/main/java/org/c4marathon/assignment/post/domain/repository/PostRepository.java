package org.c4marathon.assignment.post.domain.repository;

import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.board.domain.Board;
import org.c4marathon.assignment.board.domain.repository.BoardJpaRepository;
import org.c4marathon.assignment.board.exception.NotFoundBoardException;
import org.c4marathon.assignment.global.exception.ErrorCode;
import org.c4marathon.assignment.global.model.PageInfo;
import org.c4marathon.assignment.global.util.PageTokenUtil;
import org.c4marathon.assignment.post.domain.Post;
import org.c4marathon.assignment.post.presentation.dto.PostResponse;
import org.c4marathon.assignment.post.service.dto.PostCreateServiceRequest;
import org.c4marathon.assignment.user.domain.User;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostRepository {
    private final PostJpaRepository postJpaRepository;
    private final BoardJpaRepository boardJpaRepository;

    public PostResponse createPostByUser(PostCreateServiceRequest request, User user, Long boardId) {
        Board board = boardJpaRepository.findById(boardId)
                .orElseThrow(() -> new NotFoundBoardException(ErrorCode.NOT_FOUND_BOARD));

        Post post = Post.createByUser(request.title(), request.content(), user, board);
        postJpaRepository.save(post);

        return PostResponse.of(boardId, post);
    }

    public PostResponse createPostByGuest(PostCreateServiceRequest request, Long boardId) {
        Board board = boardJpaRepository.findById(boardId)
                .orElseThrow(() -> new NotFoundBoardException(ErrorCode.NOT_FOUND_BOARD));


        Post post = Post.createByGuest(
                request.title(),
                request.content(),
                request.guestName(),
                request.guestPassword(),
                board
        );
        postJpaRepository.save(post);

        return PostResponse.of(boardId, post);
    }

    public PageInfo<PostResponse> findAllPostWithoutPageToken(int count) {
        List<PostResponse> data = postJpaRepository.findAllPost(count + 1).stream()
                .map(PostResponse::of)
                .toList();

        return PageInfo.of(data, count, PostResponse::createdAt, PostResponse::postId);
    }

    public PageInfo<PostResponse> findAllPostWithPageToken(String pageToken, int count) {

        var pageData = PageTokenUtil.decodePageToken(pageToken, LocalDateTime.class, Long.class);
        var createdAt = pageData.getLeft();
        var postId = pageData.getRight();

        List<PostResponse> data = postJpaRepository.findAllPostWithPageToken(createdAt, postId, count + 1).stream()
                .map(PostResponse::of)
                .toList();

        return PageInfo.of(data, count, PostResponse::createdAt, PostResponse::postId);
    }

    public PageInfo<PostResponse> findPostByBoardIdWithoutPageToken(Long boardId, int count) {
        Board board = boardJpaRepository.findById(boardId)
                .orElseThrow(() -> new NotFoundBoardException(ErrorCode.NOT_FOUND_BOARD));

        return null;
    }

    public PageInfo<PostResponse> findPostByBoardIdWithPageToken(Long boardId, String pageToken, int count) {
        return null;
    }

}
