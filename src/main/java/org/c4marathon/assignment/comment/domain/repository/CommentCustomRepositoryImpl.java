package org.c4marathon.assignment.comment.domain.repository;

import static org.c4marathon.assignment.comment.domain.QComment.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.c4marathon.assignment.comment.domain.Comment;
import org.c4marathon.assignment.comment.domain.dto.CommentBlock;
import org.c4marathon.assignment.comment.dto.CommentGetAllResponse;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class CommentCustomRepositoryImpl implements CommentCustomRepository {

	private final JPAQueryFactory queryFactory;

	public CommentCustomRepositoryImpl(JPAQueryFactory queryFactory) {
		this.queryFactory = queryFactory;
	}

	@Override
	public Optional<Comment> findNotDeletedById(Long id) {
		return Optional.ofNullable(queryFactory.selectFrom(comment)
			.where(
				comment.id.eq(id),
				comment.isDeleted.eq(false)
			)
			.fetchOne());
	}

	@Override
	public List<CommentGetAllResponse> findComments(long boardId, int limit) {

		List<CommentBlock> commentBlocks = queryFactory.select(
				Projections.constructor(CommentBlock.class, comment.id, comment.content,
					comment.writerName, comment.createdDate, comment.lastModifiedDate,
					comment.parentComment.id, comment.path))
			.from(comment)
			.where(comment.isDeleted.eq(false)
				.and(comment.board.id.eq(boardId)))
			.orderBy(comment.path.desc(), comment.createdDate.desc())
			.limit(limit)
			.fetch();

		return mapToDto(commentBlocks);
	}

	@Override
	public List<CommentGetAllResponse> findCommentsWithPageToken(long boardId, LocalDateTime createdDate, String path,
		int limit) {
		List<CommentBlock> commentBlocks = queryFactory.select(
				Projections.constructor(CommentBlock.class, comment.id, comment.content,
					comment.writerName, comment.createdDate, comment.lastModifiedDate,
					comment.parentComment.id, comment.path))
			.from(comment)
			.where(comment.isDeleted.eq(false)
				.and(comment.board.id.eq(boardId))
				.and((comment.path.lt(path))
					.or(comment.path.eq(path).and(comment.createdDate.lt(createdDate)))))
			.orderBy(comment.path.desc(), comment.createdDate.desc())
			.limit(limit)
			.fetch();

		return mapToDto(commentBlocks);
	}

	private List<CommentGetAllResponse> mapToDto(List<CommentBlock> commentBlocks) {

		Map<Long, CommentGetAllResponse> commentMap = commentBlocks.stream()
			.collect(Collectors.toMap(CommentBlock::commentId, block -> new CommentGetAllResponse(
				block.commentId(),
				block.content(),
				block.writerName(),
				block.createdDate(),
				block.lastModifiedDate(),
				new java.util.ArrayList<>()
			)));

		List<CommentGetAllResponse> rootComments = new ArrayList<>();

		for (CommentBlock block : commentBlocks) {
			CommentGetAllResponse currentComment = commentMap.get(block.commentId());
			if (block.parentId() == null) {
				rootComments.add(currentComment);
			} else {
				CommentGetAllResponse parentComment = commentMap.get(block.parentId());
				if (parentComment != null) {
					parentComment.childComment().add(currentComment);
				} else {
					// 부모 댓글이 삭제된 경우
					CommentGetAllResponse deletedParentComment = new CommentGetAllResponse(
						block.parentId(),
						"삭제된 댓글입니다",
						null,
						null,
						null,
						new ArrayList<>()
					);
					commentMap.put(block.parentId(), deletedParentComment);
					deletedParentComment.childComment().add(currentComment);
					rootComments.add(deletedParentComment);
				}
			}
		}

		return rootComments;
	}
}
