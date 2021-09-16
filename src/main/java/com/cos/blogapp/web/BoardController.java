package com.cos.blogapp.web;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cos.blogapp.domain.board.Board;
import com.cos.blogapp.domain.board.BoardRepository;
import com.cos.blogapp.domain.user.User;
import com.cos.blogapp.handler.ex.MyNotFoundException;
import com.cos.blogapp.util.Script;
import com.cos.blogapp.web.dto.BoardSaveReqDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller //컴퍼넌트 스캔 (스프링) IoC
public class BoardController {
	
	private final BoardRepository boardRepository;
	private final HttpSession session;
	
	
	// 쿼리스트링, 패스var => 디비 where에 걸림
	//1.컨트롤러 선정, 2.http Method 선정, 3. 받을 데이터가 있는지 (body, 쿼리스트링 , 패스 var)
	//4. db에 접근을 해야하면 Model접근하기. 아니면 else model에 접근할 필요가 없다.
	@GetMapping("/board/{id}")
	public String detail(@PathVariable int id, Model model) {
		// select * from board where id = :id
		
		//1. orElse 는 값을 찾으면 board가 리턴, 못찾으면 괄호안(new Board)이 들어간다.
//		Board boardEntity = boardRepository.findById(id)
//				.orElse(new Board(100,"글없어요","글없어요",null));
		
		//2. orElseThrow
	 //exception(여기선 오류로그)를 화면에 던진다.
		Board boardEntity = boardRepository.findById(id)
				.orElseThrow( ()-> new MyNotFoundException(id+"못찾았땅 ㅠ") );
					
		
		model.addAttribute("boardEntity",boardEntity);
		
		return "board/detail";
	}
	
	
	
	
	@PostMapping("/board")
	public @ResponseBody String save(@Valid BoardSaveReqDto dto, BindingResult bindingResult) {
		
		User principal = (User) session.getAttribute("principal");
		
		if(principal == null) {
			return Script.back("잘못된 접근");
		}
		
		
		if(bindingResult.hasErrors()) {//haserrors는 boolean = true or false로 리턴함.
			Map<String, String> errorMap = new HashMap<>();   //map은 키값으로 찾음
			for(FieldError error:bindingResult.getFieldErrors()) {
				errorMap.put(error.getField(),error.getDefaultMessage());
				System.out.println("필드:"+error.getField());
				System.out.println("메세지:"+error.getDefaultMessage());
			}
			//model.addAttribute("errorMap", errorMap);
			return Script.back(errorMap.toString());
		}
		
		
		
		
		//System.out.println(dto.getTitle());
		
		System.out.println(dto.toEntity(principal));
		
		boardRepository.save(dto.toEntity(principal));
		return Script.href("/","글쓰기성공");
	}
	
	
	@GetMapping("/board/saveForm")
	public String saveForm() {
		
		return "board/saveForm";
	}
	
	
	@GetMapping("/board")
	public String home(Model model, int page) {
		
		//첫번째 방법
		//if(page==null) {}    == int 는 null이 될 수 없음.
		/*
		 * page를 Integer로 하고,
		 * if(page == null) { System.out.println("page값이 null 입니다."); page=0; }
		 */
		
		//두번째 방법
		//views/board 폴더에 home.jsp를 복사하고 list로 이름변경
		
		
		
		PageRequest pageRequest = PageRequest.of(page, 3,Sort.by(Sort.Direction.DESC, "id"));
		
		Page<Board> boardsEntity = boardRepository.findAll(pageRequest); //**영속화된 오브젝트
		model.addAttribute("boardsEntity",boardsEntity);
		//System.out.println(boardsEntity.get(0).getUser().getUsername());
		
		return "board/list";
	}


}
