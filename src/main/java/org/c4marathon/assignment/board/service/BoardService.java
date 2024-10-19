package org.c4marathon.assignment.board.service;

import static org.c4marathon.assignment.board.domain.WriterType.*;
import static org.c4marathon.assignment.global.dto.DeletionReason.*;

import java.time.Clock;
import java.util.List;

import org.c4marathon.assignment.board.domain.Boards;
import org.c4marathon.assignment.board.domain.repository.BoardRepository;
import org.c4marathon.assignment.board.dto.BoardCreateRequest;
import org.c4marathon.assignment.board.dto.BoardDeleteRequest;
import org.c4marathon.assignment.board.dto.BoardGetAllResponse;
import org.c4marathon.assignment.board.dto.BoardGetOneResponse;
import org.c4marathon.assignment.board.dto.BoardUpdateRequest;
import org.c4marathon.assignment.board.dto.PageInfo;
import org.c4marathon.assignment.board.exception.NotFoundImgException;
import org.c4marathon.assignment.board.service.mapper.BoardMapper;
import org.c4marathon.assignment.global.utils.ImageUtils;
import org.c4marathon.assignment.img.domain.repository.ImgRepository;
import org.c4marathon.assignment.img.service.S3Service;
import org.c4marathon.assignment.user.domain.Users;
import org.c4marathon.assignment.user.exception.NotWriterException;
import org.c4marathon.assignment.user.exception.WrongPasswordException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {

	private final BoardRepository boardRepository;
	private final ImgRepository imgRepository;
	private final S3Service s3Service;
	private final Clock clock;

	@Transactional
	public Long createBoardAsUser(BoardCreateRequest request, Users users) {
		Boards board = BoardMapper.toBoard(request, users);
		return createBoard(boardRepository.save(board));
	}

	@Transactional
	public Long createBoardAsGuest(BoardCreateRequest request) {
		Boards board = BoardMapper.toBoard(request);
		return createBoard(boardRepository.save(board));
	}

	private Long createBoard(Boards board) {
		List<String> newImgNames = getImgNamesByContent(board.getContent());

		processNewImg(board, newImgNames);

		return board.getId();
	}

	@Transactional
	public void updateBoardAsUser(Long id, BoardUpdateRequest request, String writerName) {
		Boards board = boardRepository.getById(id);

		board.updateBoard(request.content(), request.title());

		if (board.getWriterType() == USER && !board.getWriterName().equals(writerName))
			throw new NotWriterException();

		updateImg(board, request);
	}

	@Transactional
	public void updateBoardAsGuest(Long id, BoardUpdateRequest request) {
		Boards board = boardRepository.getById(id);

		board.updateBoard(request.content(), request.title());

		if (board.getWriterType() == GUEST && !board.getPassword().equals(request.password()))
			throw new WrongPasswordException();

		updateImg(board, request);
	}

	private void updateImg(Boards board, BoardUpdateRequest request) {

		List<String> existingImgNames = imgRepository.getFileNamesByBoardId(board.getId());

		List<String> updatedImgNames = getImgNamesByContent(request.content());

		List<String> deletedImgNames = existingImgNames.stream()
			.filter(fileName -> !updatedImgNames.contains(fileName))
			.toList();

		List<String> newImgNames = updatedImgNames.stream()
			.filter(fileName -> !existingImgNames.contains(fileName))
			.toList();

		processNewImg(board, newImgNames);

		processDeletedImg(deletedImgNames);
	}

	@Transactional
	public void deleteBoardAsUser(Long id, String writerName) {
		Boards board = boardRepository.getById(id);

		if (!board.getWriterName().equals(writerName))
			throw new NotWriterException();

		board.deleteBoard(DELETED_BY_MEMBER.getMessage(), clock);

		List<String> existingImgNames = imgRepository.getFileNamesByBoardId(board.getId());

		processDeletedImg(existingImgNames);
	}

	@Transactional
	public void deleteBoardAsGuest(Long id, BoardDeleteRequest request) {
		Boards board = boardRepository.getById(id);

		if (!board.getPassword().equals(request.password()))
			throw new WrongPasswordException();

		board.deleteBoard(DELETED_BY_MEMBER.getMessage(), clock);
		
		List<String> existingImgNames = imgRepository.getFileNamesByBoardId(board.getId());

		processDeletedImg(existingImgNames);
	}

	@Transactional(readOnly = true)
	public PageInfo<BoardGetAllResponse> getAllBoards(String pageToken, int size) {
		if (pageToken == null) {
			return boardRepository.findBoardsWithoutPageToken(size);
		} else {
			return boardRepository.findBoardsWithPageToken(pageToken, size);
		}
	}

	@Transactional(readOnly = true)
	public BoardGetOneResponse getOneBoard(Long id) {
		Boards boards = boardRepository.getById(id);
		return BoardMapper.toDto(boards);
	}

	private void processNewImg(Boards board, List<String> newImgNames) {
		validateFileNames(newImgNames);

		imgRepository.setBoardByFileName(newImgNames, board);
	}

	private void processDeletedImg(List<String> deletedImgNames) {
		s3Service.deleteImages(deletedImgNames);

		imgRepository.deleteByFileNames(deletedImgNames);
	}

	private void validateFileNames(List<String> fileNames) {
		List<String> invalidFileNames = fileNames.stream()
			.filter(fileName -> !imgRepository.existsByFileName(fileName))
			.toList();

		if (!invalidFileNames.isEmpty()) {
			throw new NotFoundImgException();
		}
	}

	private List<String> getImgNamesByContent(String content) {
		List<String> imgUrls = ImageUtils.extractImgUrls(content);

		return imgUrls.stream()
			.filter(s3Service::validateUrl)
			.map(url -> url.substring(url.lastIndexOf('/') + 1))
			.toList();
	}

}
