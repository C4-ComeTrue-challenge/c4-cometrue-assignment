package org.c4marathon.assignment.board.service.mapper;

import static org.c4marathon.assignment.board.domain.WriterType.*;

import org.c4marathon.assignment.board.domain.Boards;
import org.c4marathon.assignment.board.dto.BoardCreateRequest;
import org.c4marathon.assignment.board.dto.BoardGetOneResponse;
import org.c4marathon.assignment.user.domain.Users;

public class BoardMapper {
	public static Boards toBoard(BoardCreateRequest request, Users users) {
		return Boards.builder()
			.title(request.title())
			.content(request.content())
			.users(users)
			.writerType(USER)
			.build();
	}

	public static Boards toBoard(BoardCreateRequest request) {
		return Boards.builder()
			.title(request.title())
			.content(request.content())
			.writerName(request.writerName())
			.password(request.password())
			.writerType(GUEST)
			.build();
	}

	public static BoardGetOneResponse toDto(Boards boards) {
		return new BoardGetOneResponse(boards.getId(), boards.getContent(), boards.getTitle(), boards.getWriterName(),
			boards.getCreatedDate(), boards.getLastModifiedDate());
	}

}
