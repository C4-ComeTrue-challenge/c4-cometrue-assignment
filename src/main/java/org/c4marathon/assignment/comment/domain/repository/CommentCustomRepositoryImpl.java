package org.c4marathon.assignment.comment.domain.repository;

import static org.c4marathon.assignment.comment.domain.QComment.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.c4marathon.assignment.comment.domain.Comment;
import org.c4marathon.assignment.comment.dto.CommentGetAllResponse;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class CommentCustomRepositoryImpl implements CommentCustomRepository {

	private final JPAQueryFactory queryFactory;

	public CommentCustomRepositoryImpl(JPAQueryFactory queryFactory) {
		this.queryFactory = queryFactory;
	}

	@Override
	public Optional<Comment> findNotDeletedById(Long id) {
		return Optional.ofNullable(
			queryFactory.selectFrom(comment).where(comment.id.eq(id), comment.isDeleted.eq(false)).fetchOne());
	}

	@Override
	public List<CommentGetAllResponse> findComments(long boardId, int limit) {

		List<CommentGetAllResponse> commentBlocks = queryFactory.select(
				Projections.constructor(CommentGetAllResponse.class, comment.id, comment.content, comment.writerName,
					comment.createdDate, comment.lastModifiedDate, comment.parentComment.id, comment.path))
			.from(comment)
			.where(comment.isDeleted.eq(false).and(comment.board.id.eq(boardId)))
			.orderBy(
				Expressions.stringTemplate("case when {0} = '' then {1} else concat({0}, '/', {1}) end", comment.path,
					comment.id).asc(), comment.createdDate.desc())
			.limit(limit)
			.fetch();

		return handleDeletedParentComments(commentBlocks);
	}

	@Override
	public List<CommentGetAllResponse> findCommentsWithPageToken(long boardId, LocalDateTime createdDate, String path,
		int limit) {
		List<CommentGetAllResponse> commentBlocks = queryFactory.select(
				Projections.constructor(CommentGetAllResponse.class, comment.id, comment.content, comment.writerName,
					comment.createdDate, comment.lastModifiedDate, comment.parentComment.id, comment.path))
			.from(comment)
			.where(comment.isDeleted.eq(false)
				.and(comment.board.id.eq(boardId))
				.and(createPagingCondition(path, createdDate)))
			.orderBy(
				Expressions.stringTemplate("case when {0} = '' then {1} else concat({0}, '/', {1}) end", comment.path,
					comment.id).asc(), comment.createdDate.desc())
			.limit(limit)
			.fetch();

		return handleDeletedParentComments(commentBlocks);
	}

	private BooleanExpression createPagingCondition(String path, LocalDateTime createdDate) {
		if (path == null) {
			return comment.createdDate.gt(createdDate);
		}
		return comment.path.gt(path).or(comment.path.eq(path).and(comment.createdDate.gt(createdDate)));
	}

	private List<CommentGetAllResponse> handleDeletedParentComments(List<CommentGetAllResponse> commentBlocks) {
		// 1. 모든 댓글을 Map에 저장하여 쉽게 부모 댓글의 존재 여부를 확인
		Map<Long, CommentGetAllResponse> commentMap = commentBlocks.stream()
			.collect(Collectors.toMap(CommentGetAllResponse::commentId, comment -> comment));

		// 2. 각 댓글을 순회하며 부모 댓글이 삭제되었는지 확인하고 필요하면 내용을 수정
		return commentBlocks.stream()
			.map(comment -> {
				if (comment.parentId() != null) {
					// 부모 댓글이 현재 댓글 목록에 없는 경우 ("삭제된 댓글입니다" 처리)
					if (!commentMap.containsKey(comment.parentId())) {
						Optional<Comment> parent = findNotDeletedById(comment.commentId());
						if (parent.isEmpty()) {
							return new CommentGetAllResponse(
								comment.commentId(),
								"삭제된 댓글입니다",
								comment.writerName(),
								comment.createdDate(),
								comment.lastModifiedDate(),
								comment.parentId(),
								comment.path()
							);
						} else {
							return new CommentGetAllResponse(
								parent.get().getId(),
								parent.get().getContent(),
								parent.get().getWriterName(),
								parent.get().getCreatedDate(),
								parent.get().getLastModifiedDate(),
								parent.get().getParentComment().getId(),
								parent.get().getPath()
							);
						}
					}
				}
				// 부모 댓글이 존재하는 경우 그대로 반환
				return comment;
			})
			.collect(Collectors.toList());
	}
}
