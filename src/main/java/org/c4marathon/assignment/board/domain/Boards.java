package org.c4marathon.assignment.board.domain;

import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import org.c4marathon.assignment.global.BaseTimeEntity;
import org.c4marathon.assignment.user.domain.Users;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
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

	@Lob
	@Column(nullable = false)
	private String content;

	@Column(nullable = false, length = 100)
	private String title;

	@Column(nullable = false, length = 20)
	private String writerName;

	@Column
	private String password;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private Users users;

	public boolean isWrittenByUser() {
		return this.users != null;
	}

	@Builder
	public Boards(String title, String content, String writerName, String password, Users users) {
		this.title = title;
		this.content = content;
		this.writerName = writerName;
		this.password = password;
		this.users = users;

		if (users != null) {
			this.writerName = users.getNickname();
		}
	}
}
