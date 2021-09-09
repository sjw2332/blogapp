package com.cos.blogapp.web;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.cos.blogapp.domain.board.Board;
import com.cos.blogapp.domain.board.BoardRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller //컴퍼넌트 스캔 (스프링) IoC
public class BoardController {
	
	private final BoardRepository boardRepository;
	
	
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
