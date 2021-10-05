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
import org.springframework.web.bind.annotation.ResponseBody;

import com.cos.blogapp.domain.board.Board;
import com.cos.blogapp.domain.board.BoardRepository;
import com.cos.blogapp.domain.user.User;
import com.cos.blogapp.handler.ex.MyAsyncNotFoundException;
import com.cos.blogapp.handler.ex.MyNotFoundException;
import com.cos.blogapp.util.Script;
import com.cos.blogapp.web.dto.BoardSaveReqDto;
import com.cos.blogapp.web.dto.CMRespDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor // final이 붙은 필드에 대한 생성자가 만들어진다.
@Controller // 컴퍼넌트 스캔 (스프링) IoC
public class BoardController {

	// DI
	private final BoardRepository boardRepository;
	private final HttpSession session;
	
	
	//API 요청( AJAX 등)
	@DeleteMapping("/board/{id}")
	public @ResponseBody CMRespDto deleteById(@PathVariable int id) {
		
		//인증이 된 사람만 함수 접근 가능  (로그인 된 사람)
		
		User principal = (User)session.getAttribute("principal");
		
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
		
		return new CMRespDto<>(1,"성공",null);  //오브젝트를 return하면 json이 된다.
	}
	
	
	
	// 쿼리스트링, 패스var => 디비 where 에 걸리는 친구들!!
	// 1. 컨트롤러 선정, 2. Http Method 선정, 3. 받을 데이터가 있는지!! (body, 쿼리스트링, 패스var)
	// 4. 디비에 접근을 해야하면 Model 접근하기 orElse Model에 접근할 필요가 없다.
	@GetMapping("/board/{id}")
	public String detail(@PathVariable int id, Model model) {
		// select * from board where id = :id
		// 1. orElse 는 값을 찾으면 Board가 리턴, 못찾으면 (괄호안 내용 리턴)
//		Board boardEntity =  boardRepository.findById(id)
//				.orElse(new Board(100, "글없어요", "글없어요", null));
		
		// 2. orElseThrow
		Board boardEntity =  boardRepository.findById(id)
				.orElseThrow(()-> new MyNotFoundException(id+" 못찾았어요") );
		
		model.addAttribute("boardEntity", boardEntity);
		return "board/detail";
	}
	
	@PostMapping("/board")
	public @ResponseBody String save(@Valid BoardSaveReqDto dto, BindingResult bindingResult) {

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
		
		System.out.println(dto.getTitle());
		System.out.println(dto.getContent());
		// 10시 15분까지 -> BoardSaveReqDto 생성
		// Postman으로 테스트
		// 콘솔에 출력    
		
		
		
//		User user = new User();
//		user.setId(3);
//		boardRepository.save(dto.toEntity(user));
		
		boardRepository.save(dto.toEntity(principal));
		return Script.href("/", "글쓰기 성공");
	}
	
	
	@GetMapping("/board/saveForm")
	public String saveForm() {
		return "board/saveForm";
	}
	
	// /board?page=2
	@GetMapping("/board")
	public String home(Model model, int page) {
		
		PageRequest pageRequest = PageRequest.of(page, 3, Sort.by(Sort.Direction.DESC, "id"));

		Page<Board> boardsEntity = 
				boardRepository.findAll(pageRequest);
		model.addAttribute("boardsEntity", boardsEntity);
		//System.out.println(boardsEntity.get(0).getUser().getUsername());
		return "board/list";
	}
}
