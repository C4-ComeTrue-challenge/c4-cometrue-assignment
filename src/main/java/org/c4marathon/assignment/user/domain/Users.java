package org.c4marathon.assignment.user.domain;

import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import java.io.Serializable;
import java.time.Clock;
import java.time.LocalDateTime;

import org.c4marathon.assignment.global.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Users extends BaseTimeEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "user_id")
	private Long id;

	@Column(nullable = false, unique = true, length = 20)
	private String nickname;

	@Column(nullable = false, unique = true, length = 20)
	private String email;

	@Column(nullable = false)
	private String password;

	private boolean isDeleted;

	@Column(length = 30)
	private String deletionReason;

	private LocalDateTime deletedDate;

	@Builder
	public Users(String email, String password, String nickname) {
		this.email = email;
		this.password = password;
		this.nickname = nickname;
	}

	public void deleteUser(String deletionReason, Clock clock) {
		this.isDeleted = true;
		this.deletionReason = deletionReason;
		this.deletedDate = LocalDateTime.now(clock);
	}

}
