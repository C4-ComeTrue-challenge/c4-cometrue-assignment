package org.c4marathon.assignment.comment.domain;

import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import java.time.Clock;
import java.time.LocalDateTime;

import org.c4marathon.assignment.board.domain.Boards;
import org.c4marathon.assignment.board.domain.WriterType;
import org.c4marathon.assignment.global.BaseTimeEntity;
import org.c4marathon.assignment.user.domain.Users;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Comment extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "comment_id")
	private Long id;

	@Column(nullable = false, length = 1000)
	private String content;

	private String path;

	@Column(nullable = false, length = 20)
	private String writerName;

	@Column
	private String password;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	public WriterType writerType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private Users users;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id")
	private Comment parentComment;

	private boolean isDeleted;

	@Column(length = 30)
	private String deletionReason;

	private LocalDateTime deletedDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "board_id")
	private Boards board;

	@Builder
	public Comment(String content, String path, Boards board, Comment parent, Users users, String password,
		String writerName, WriterType writerType) {
		this.content = content;
		this.path = path;
		this.writerType = writerType;
		this.board = board;
		parentComment = parent;
		if (users != null) {
			this.users = users;
			this.writerName = users.getNickname();
		} else {
			this.writerName = writerName;
			this.password = password;
		}
	}

	public void updateComment(String content) {
		this.content = content;
	}

	public void deleteComment(String deletionReason, Clock clock) {
		this.isDeleted = true;
		this.deletionReason = deletionReason;
		this.deletedDate = LocalDateTime.now(clock);
	}
}
