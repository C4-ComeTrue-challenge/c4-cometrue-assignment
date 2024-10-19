package org.c4marathon.assignment.board.domain;

import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import java.time.Clock;
import java.time.LocalDateTime;

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
public class Boards extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "board_id")
	private Long id;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String content;

	@Column(nullable = false, length = 100)
	private String title;

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

	private boolean isDeleted;

	@Column(length = 30)
	private String deletionReason;

	private LocalDateTime deletedDate;

	public boolean isWrittenByUser() {
		return this.users != null;
	}

	@Builder
	public Boards(String title, String content, String writerName, String password, Users users,
		WriterType writerType) {
		this.title = title;
		this.content = content;
		this.writerType = writerType;

		if (users != null) {
			this.users = users;
			this.writerName = users.getNickname();
		} else {
			this.writerName = writerName;
			this.password = password;
		}
	}

	public void updateBoard(String content, String title) {
		this.content = content;
		this.title = title;
	}

	public void deleteBoard(String deletionReason, Clock clock) {
		this.isDeleted = true;
		this.deletionReason = deletionReason;
		this.deletedDate = LocalDateTime.now(clock);
	}
}
