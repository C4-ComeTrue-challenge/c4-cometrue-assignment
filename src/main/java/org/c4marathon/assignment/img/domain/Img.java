package org.c4marathon.assignment.img.domain;

import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import org.c4marathon.assignment.board.domain.Boards;
import org.c4marathon.assignment.global.BaseTimeEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class Img extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "img_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JoinColumn(name = "board_id")
	private Boards board;

	@Column(nullable = false, length = 45)
	private String fileName;

	@Builder
	public Img(String fileName, Boards board) {
		this.fileName = fileName;
		this.board = board;
	}

	public void setBoard(Boards board) {
		this.board = board;
	}
}
