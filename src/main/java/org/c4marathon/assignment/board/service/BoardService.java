package org.c4marathon.assignment.board.service;

import static org.c4marathon.assignment.board.domain.WriterType.*;
import static org.c4marathon.assignment.global.dto.DeletionReason.*;

import java.time.Clock;
import java.util.List;
import java.util.stream.Collectors;

import org.c4marathon.assignment.board.domain.Boards;
import org.c4marathon.assignment.board.domain.repository.BoardRepository;
import org.c4marathon.assignment.board.dto.BoardCreateRequest;
import org.c4marathon.assignment.board.dto.BoardGetAllResponse;
import org.c4marathon.assignment.board.dto.BoardGetOneResponse;
import org.c4marathon.assignment.board.dto.BoardUpdateRequest;
import org.c4marathon.assignment.board.dto.PageInfo;
import org.c4marathon.assignment.board.service.mapper.BoardMapper;
import org.c4marathon.assignment.global.utils.ImageUtils;
import org.c4marathon.assignment.img.domain.Img;
import org.c4marathon.assignment.img.domain.repository.ImgRepository;
import org.c4marathon.assignment.img.exception.NotFoundImgException;
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
	public Long createBoard(BoardCreateRequest request, Users user) {
		Boards board = createBoardEntity(request, user);
		boardRepository.save(board);
		saveBoardImages(board);
		return board.getId();
	}

	@Transactional
	public void updateBoard(Long id, BoardUpdateRequest request, String writerName) {
		Boards board = boardRepository.getById(id);
		validateUserOrGuest(board, writerName, request.password());
		board.updateBoard(request.content(), request.title());
		updateImg(board, request);
	}

	@Transactional
	public void deleteBoard(Long id, String writerName, String password) {
		Boards board = boardRepository.getById(id);
		validateUserOrGuest(board, writerName, password);
		board.deleteBoard(DELETED_BY_MEMBER.getMessage(), clock);
		deleteBoardImages(board);
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

	private Boards createBoardEntity(BoardCreateRequest request, Users user) {
		if (user == null)
			return BoardMapper.toBoard(request);
		return BoardMapper.toBoard(request, user);
	}

	private void saveBoardImages(Boards board) {
		List<String> newImgNames = getImgNamesByContent(board.getContent());
		List<Img> images = convertToImgList(newImgNames, board);
		imgRepository.saveAll(images);
	}

	private void updateImg(Boards board, BoardUpdateRequest request) {
		List<String> existingImgNames = imgRepository.getFileNamesByBoardId(board.getId());
		List<String> updatedImgNames = getImgNamesByContent(request.content());

		//기존 이미지 중에서 삭제될 이미지는 삭제처리
		List<String> deletedImgNames = existingImgNames.stream()
			.filter(fileName -> !updatedImgNames.contains(fileName))
			.toList();
		imgRepository.deleteByFileNames(deletedImgNames);

		//새로운 이미지는 저장
		List<String> newImgNames = updatedImgNames.stream()
			.filter(fileName -> !existingImgNames.contains(fileName))
			.toList();

		List<Img> images = convertToImgList(newImgNames, board);
		imgRepository.saveAll(images);
	}

	private List<String> getImgNamesByContent(String content) {
		List<String> imgUrls = ImageUtils.extractImgUrls(content);

		boolean allValid = imgUrls.stream().allMatch(s3Service::validateUrl);
		if (!allValid) {
			throw new NotFoundImgException();
		}

		return imgUrls.stream().map(url -> url.substring(url.lastIndexOf('/') + 1)).toList();
	}

	private void validateUserOrGuest(Boards board, String writerName, String password) {
		if (writerName == null) { // GUEST
			if (board.getWriterType() == GUEST && !board.getPassword().equals(password)) {
				throw new WrongPasswordException();
			}
		} else { // USER
			if (board.getWriterType() == USER && !board.getWriterName().equals(writerName)) {
				throw new NotWriterException();
			}
		}
	}

	private void deleteBoardImages(Boards board) {
		List<String> existingImgNames = imgRepository.getFileNamesByBoardId(board.getId());
		imgRepository.deleteByFileNames(existingImgNames);
	}

	private List<Img> convertToImgList(List<String> newImgNames, Boards board) {
		return newImgNames.stream()
			.map(fileName -> Img.builder().fileName(fileName).board(board).build())
			.collect(Collectors.toList());
	}
}
