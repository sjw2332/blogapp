package com.cos.blogapp.service;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.FieldError;

import com.cos.blogapp.domain.board.Board;
import com.cos.blogapp.domain.board.BoardRepository;
import com.cos.blogapp.domain.comment.Comment;
import com.cos.blogapp.domain.comment.CommentRepository;
import com.cos.blogapp.domain.user.User;
import com.cos.blogapp.handler.ex.MyAsyncNotFoundException;
import com.cos.blogapp.handler.ex.MyNotFoundException;
import com.cos.blogapp.web.dto.BoardSaveReqDto;
import com.cos.blogapp.web.dto.CommentSaveReqDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor 
@Service
public class BoardService {
	
	public final BoardRepository boardRepository;
	private final CommentRepository commentRepository;
	
	
	//db에 연결하면 무조건 서비스 연결
	
	
	public void 댓글등록(int boardId, CommentSaveReqDto dto, User principal ) {
		Board boardEntity = boardRepository.findById(boardId)
				.orElseThrow(()-> new MyNotFoundException("해당 글을 찾을 수 없습니다."));
		
		Comment comment = new Comment();
		comment.setContent(dto.getContent());
		comment.setUser(principal);
		comment.setBoard(boardEntity);
		
		commentRepository.save(comment);
		
	}
	
	
	
	@Transactional(rollbackFor = MyAsyncNotFoundException.class)
	public void 게시글수정(int id, User principal, BoardSaveReqDto dto) {
		
		//requestbody는 urlencoded가 아니라 있는 그대로 받아옴
		 
				
		//권한
		
		Board boardEntity = boardRepository.findById(id).
				orElseThrow(()-> new MyNotFoundException("아이디를 찾을 수 없습니다.") ); 
		
		if (principal.getId() != boardEntity.getUser().getId()) {
			throw new MyAsyncNotFoundException("해당 글의 작성자가 아닙니다.");
		}
		
		Board board = dto.toEntity(principal);
		board.setId(id);  //update의 핵심. 
		
		boardRepository.save(board); 
		
	}
	
	public Board 게시글수정페이지이동(int id) {
		//게시글정보를 가지고 가야함
		Board boardEntity = boardRepository.findById(id)
				.orElseThrow( ()-> new MyNotFoundException(id+"번호의 게시글을 찾을 수 없습니다.")  );

		return boardEntity;
		
	}
	
	@Transactional(rollbackFor = MyAsyncNotFoundException.class)
	public void 게시글삭제(int id, User principal) {
		
		if(principal == null) { 
			throw new MyAsyncNotFoundException("로그인 되지 않았습니다.");
		}
		
		//복습
		//권한이 있는 사람만 함수 접근 가능(principal.id == {id})
//		Board boardEntity =  boardRepository.findById(id)
//				.orElseThrow(()-> new MyNotFoundException(id+" 못찾았어요") );
		
		Board boardEntity = boardRepository.findById(id)
			      .orElseThrow(()-> new MyAsyncNotFoundException("해당글을 찾을 수 없습니다."));
			   if(principal.getId() != boardEntity.getUser().getId()) {
			      throw new MyAsyncNotFoundException("해당글을 삭제할 권한이 없습니다.");
			   }

		try {
			boardRepository.deleteById(id);
			
		} catch (Exception e) {
			throw new MyAsyncNotFoundException(id + "를 찾을수 없습니다");
		}
		
		
	}
	
	public Board 상세보기(int id) {
		// select * from board where id = :id
		// 1. orElse 는 값을 찾으면 Board가 리턴, 못찾으면 (괄호안 내용 리턴)
		//	Board boardEntity =  boardRepository.findById(id)
		//			.orElse(new Board(100, "글없어요", "글없어요", null));
		
		// 2. orElseThrow
		Board boardEntity =  boardRepository.findById(id)
				.orElseThrow(()-> new MyNotFoundException(id+" 못찾았어요") );
		
		return boardEntity;
	}
	
	@Transactional(rollbackFor = MyAsyncNotFoundException.class)
	public void 게시글등록( BoardSaveReqDto dto, User principal) {
		
		boardRepository.save(dto.toEntity(principal));
		
	}
	
	
	public Page<Board> 게시글목록(int page) {
		PageRequest pageRequest = PageRequest.of(page, 3, Sort.by(Sort.Direction.DESC, "id"));

		Page<Board> boardsEntity = boardRepository.findAll(pageRequest);
				
		return boardsEntity;
	}
	
	

}
