package org.c4marathon.assignment.board.service;

import java.util.List;

import org.c4marathon.assignment.board.domain.Boards;
import org.c4marathon.assignment.board.domain.repository.BoardRepository;
import org.c4marathon.assignment.board.dto.BoardCreateRequest;
import org.c4marathon.assignment.board.dto.BoardGetAllResponse;
import org.c4marathon.assignment.board.dto.BoardGetOneResponse;
import org.c4marathon.assignment.board.exception.NotFoundImgException;
import org.c4marathon.assignment.global.utils.ImageUtils;
import org.c4marathon.assignment.img.domain.repository.ImgRepository;
import org.c4marathon.assignment.user.domain.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {

	private final BoardRepository boardRepository;
	private final ImgRepository imgRepository;

	@Transactional
	public long createBoardAsUser(BoardCreateRequest request, Users users) {
		Boards board = toBoard(request, users);

		List<String> imageUrls = ImageUtils.extractImageUrls(request.content());
		List<String> fileNames = ImageUtils.extractFileNamesFromImageUrls(imageUrls);

		validateImg(imageUrls, fileNames);

		imgRepository.updateImgBoardId(fileNames, board.getId());
		return boardRepository.save(board).getId();
	}

	@Transactional
	public long createBoardAsGuest(BoardCreateRequest request) {
		Boards board = toBoard(request);
		return boardRepository.save(board).getId();
	}

	@Transactional(readOnly = true)
	public Page<BoardGetAllResponse> getAllBoards(Pageable pageable) {
		return boardRepository.getAll(pageable);
	}

	@Transactional(readOnly = true)
	public BoardGetOneResponse getOneBoard(Long id) {
		Boards boards = boardRepository.getById(id);
		return toDto(boards);
	}

	private void validateImg(List<String> imgUrls, List<String> fileNames) {
		if (imgUrls.size() != fileNames.size()) {
			throw new NotFoundImgException();
		}

		List<String> invalidFileNames = fileNames.stream()
			.filter(fileName -> !imgRepository.existsByFileName(fileName))
			.toList();

		if (!invalidFileNames.isEmpty()) {
			throw new NotFoundImgException();
		}
	}

	private Boards toBoard(BoardCreateRequest request, Users users) {
		return Boards.builder().title(request.title()).content(request.content()).users(users).build();
	}

	private Boards toBoard(BoardCreateRequest request) {
		return Boards.builder()
			.title(request.title())
			.content(request.content())
			.writerName(request.writerName())
			.password(request.password())
			.build();
	}

	private BoardGetOneResponse toDto(Boards boards) {
		return new BoardGetOneResponse(boards.getId(), boards.getContent(), boards.getTitle(), boards.getWriterName(),
			boards.getCreatedDate(), boards.getLastModifiedDate());
	}

}
