package com.cos.blogapp.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cos.blogapp.domain.board.Board;
import com.cos.blogapp.domain.board.BoardRepository;
import com.cos.blogapp.domain.comment.Comment;
import com.cos.blogapp.domain.comment.CommentRepository;
import com.cos.blogapp.domain.user.User;
import com.cos.blogapp.handler.ex.MyAsyncNotFoundException;
import com.cos.blogapp.handler.ex.MyNotFoundException;
import com.cos.blogapp.service.BoardService;
import com.cos.blogapp.util.Script;
import com.cos.blogapp.web.dto.BoardSaveReqDto;
import com.cos.blogapp.web.dto.CMRespDto;
import com.cos.blogapp.web.dto.CommentSaveReqDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor // final이 붙은 필드에 대한 생성자가 만들어진다.
@Controller // 컴퍼넌트 스캔 (스프링) IoC
public class BoardController {

	// DI

	private final BoardService boardService;
	private final HttpSession session;
	
	
	//댓글작성
	@PostMapping("/board/{boardId}/comment") 
	public String commentSave(@PathVariable int boardId, CommentSaveReqDto dto) {
		
		User principal = (User) session.getAttribute("principal");
		System.out.println(principal);
		
		boardService.댓글등록(boardId, dto, principal);
		
		return "redirect:/board/"+boardId; 
	}
	
	
	
	//게시글 수정
	@PutMapping("/board/{id}")
	public @ResponseBody CMRespDto<String> update(@PathVariable int id, @Valid @RequestBody BoardSaveReqDto dto, BindingResult bindingResult){
		
		User principal = (User) session.getAttribute("principal");
		//인증
		if (principal == null) {
			throw new MyAsyncNotFoundException("인증되지 않았습니다.");
		}
		
		//유효성
		if(bindingResult.hasErrors()) {
			Map<String, String> errorMap = new HashMap<>();
			for(FieldError error : bindingResult.getFieldErrors()) {
				errorMap.put(error.getField(), error.getDefaultMessage());
			}
			throw new MyAsyncNotFoundException(errorMap.toString());
		}
		
		boardService.게시글수정(id, principal, dto);
		
		
		return new CMRespDto<>(1,"업데이트 성공",null);
	}
	
	
	//게시글수정 페이지
	@GetMapping("/board/{id}/updateForm")
	public String updateForm(@PathVariable int id, Model model) {
		
		model.addAttribute("boardEntity", boardService.게시글수정페이지이동(id));
	
		return "/board/updateForm";
	}
	
	
	//게시글삭제
	//API 요청( AJAX 등)
	@DeleteMapping("/board/{id}")
	public @ResponseBody CMRespDto deleteById(@PathVariable int id) {
		
		//인증이 된 사람만 함수 접근 가능  (로그인 된 사람)
		
		User principal = (User)session.getAttribute("principal");
		
		boardService.게시글삭제(id, principal);
		
		
		return new CMRespDto<>(1,"성공",null);  //오브젝트를 return하면 json이 된다.
	}
	
	
	
	//게시글 보기
	// 쿼리스트링, 패스var => 디비 where 에 걸리는 친구들!!
	// 1. 컨트롤러 선정, 2. Http Method 선정, 3. 받을 데이터가 있는지!! (body, 쿼리스트링, 패스var)
	// 4. 디비에 접근을 해야하면 Model 접근하기 orElse Model에 접근할 필요가 없다.
	@GetMapping("/board/{id}")
	public String detail(@PathVariable int id, Model model) {
		
		
		model.addAttribute("boardEntity", boardService.상세보기(id));
		return "board/detail";
	}
	
	
	
	//글쓰기
	@PostMapping("/board")
	public @ResponseBody String save(@Valid BoardSaveReqDto dto, BindingResult bindingResult) {
		
		//------------공통로직 시작
		User principal = (User)session.getAttribute("principal");
		
		// 인증, 권한 체크 (공통로직)
		if(principal == null) { // 로그인 안됨
			return Script.href("/loginForm","잘못된 접근입니다");
		}
		
		if(bindingResult.hasErrors()) {
			Map<String, String> errorMap = new HashMap<>();
			for(FieldError error : bindingResult.getFieldErrors()) {
				errorMap.put(error.getField(), error.getDefaultMessage());
			}
			return Script.back(errorMap.toString());
		}
		
		// 10시 15분까지 -> BoardSaveReqDto 생성
		// Postman으로 테스트
		// 콘솔에 출력    
		
//		User user = new User();
//		user.setId(3);
//		boardRepository.save(dto.toEntity(user));
		
		//---------------------------공통로직 끝
		
		//핵심로직 시작-----------------------------------
		boardService.게시글등록(dto, principal);
		//핵심로직 끝---------------------------
		return Script.href("/", "글쓰기 성공");
	}
	
	
	//글쓰기 페이지
	@GetMapping("/board/saveForm")
	public String saveForm() {
		return "board/saveForm";
	}
	
	//메인화면
	// /board?page=2
	@GetMapping("/board")
	public String home(Model model, int page) {
		
		model.addAttribute("boardsEntity", boardService.게시글목록(page));
		//System.out.println(boardsEntity.get(0).getUser().getUsername());
		return "board/list";
	}
}
