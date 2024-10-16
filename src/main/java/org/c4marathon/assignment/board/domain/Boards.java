package org.c4marathon.assignment.board.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.c4marathon.assignment.global.BaseTimeEntity;
import org.c4marathon.assignment.user.domain.Users;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

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
