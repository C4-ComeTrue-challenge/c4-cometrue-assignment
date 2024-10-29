package org.c4marathon.assignment.board.service;

import static org.c4marathon.assignment.board.domain.WriterType.*;
import static org.c4marathon.assignment.global.dto.DeletionReason.*;

import java.time.Clock;
import java.util.List;
import java.util.stream.Collectors;

import org.c4marathon.assignment.board.domain.Boards;
import org.c4marathon.assignment.board.domain.repository.BoardRepository;
import org.c4marathon.assignment.board.dto.BoardCreateRequest;
import org.c4marathon.assignment.board.dto.BoardDeleteRequest;
import org.c4marathon.assignment.board.dto.BoardGetAllResponse;
import org.c4marathon.assignment.board.dto.BoardGetOneResponse;
import org.c4marathon.assignment.board.dto.BoardUpdateRequest;
import org.c4marathon.assignment.board.dto.PageInfo;
import org.c4marathon.assignment.board.service.mapper.BoardMapper;
import org.c4marathon.assignment.global.utils.ImageUtils;
import org.c4marathon.assignment.img.domain.Img;
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
		//게시글 저장
		Boards board = BoardMapper.toBoard(request, users);
		boardRepository.save(board);
		//이미지 저장
		List<String> newImgNames = getImgNamesByContent(board.getContent());
		List<Img> images = convertToImgList(newImgNames, board);
		imgRepository.saveAll(images);
		return board.getId();
	}

	@Transactional
	public Long createBoardAsGuest(BoardCreateRequest request) {
		//게시글 저장
		Boards board = BoardMapper.toBoard(request);
		boardRepository.save(board);
		//이미지 저장
		List<String> newImgNames = getImgNamesByContent(board.getContent());
		List<Img> images = convertToImgList(newImgNames, board);
		imgRepository.saveAll(images);
		return board.getId();
	}

	@Transactional
	public void updateBoardAsUser(Long id, BoardUpdateRequest request, String writerName) {
		//게시글 업데이트
		Boards board = boardRepository.getById(id);
		if (board.getWriterType() == USER && !board.getWriterName().equals(writerName))
			throw new NotWriterException();
		board.updateBoard(request.content(), request.title());

		//이미지 업데이트
		updateImg(board, request);
	}

	@Transactional
	public void updateBoardAsGuest(Long id, BoardUpdateRequest request) {
		//게시글 업데이트
		Boards board = boardRepository.getById(id);
		if (board.getWriterType() == GUEST && !board.getPassword().equals(request.password()))
			throw new WrongPasswordException();
		board.updateBoard(request.content(), request.title());

		//이미지 업데이트
		updateImg(board, request);
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

	@Transactional
	public void deleteBoardAsUser(Long id, String writerName) {
		//게시글 삭제
		Boards board = boardRepository.getById(id);
		if (!board.getWriterName().equals(writerName))
			throw new NotWriterException();
		board.deleteBoard(DELETED_BY_MEMBER.getMessage(), clock);

		//이미지 삭제
		List<String> existingImgNames = imgRepository.getFileNamesByBoardId(board.getId());
		imgRepository.deleteByFileNames(existingImgNames);
	}

	@Transactional
	public void deleteBoardAsGuest(Long id, BoardDeleteRequest request) {
		//게시글 삭제
		Boards board = boardRepository.getById(id);
		if (!board.getPassword().equals(request.password()))
			throw new WrongPasswordException();
		board.deleteBoard(DELETED_BY_MEMBER.getMessage(), clock);

		//이미지 삭제
		List<String> existingImgNames = imgRepository.getFileNamesByBoardId(board.getId());
		imgRepository.deleteByFileNames(existingImgNames);
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

	private List<String> getImgNamesByContent(String content) {
		List<String> imgUrls = ImageUtils.extractImgUrls(content);

		return imgUrls.stream()
			.filter(s3Service::validateUrl)
			.map(url -> url.substring(url.lastIndexOf('/') + 1))
			.toList();
	}

	private List<Img> convertToImgList(List<String> newImgNames, Boards board) {
		return newImgNames.stream()
			.map(fileName -> Img.builder()
				.fileName(fileName)
				.board(board)
				.build())
			.collect(Collectors.toList());
	}
}
