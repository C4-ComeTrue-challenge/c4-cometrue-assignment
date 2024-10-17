package org.c4marathon.assignment.img.domain;

import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

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
public class Img extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "img_id")
	private Long id;

	private Long boardId;

	@Column(nullable = false, length = 45)
	private String fileName;

	@Builder
	public Img(String fileName) {
		this.fileName = fileName;
	}
}
