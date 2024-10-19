package org.c4marathon.assignment.board.dto;

public record BoardUpdateRequest(

	String title,

	String content,

	String password
) {
}
