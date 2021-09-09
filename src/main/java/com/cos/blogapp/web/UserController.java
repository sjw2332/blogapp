package com.cos.blogapp.web;


import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cos.blogapp.domain.user.User;
import com.cos.blogapp.domain.user.UserRepository;
import com.cos.blogapp.util.Script;
import com.cos.blogapp.web.dto.JoinReqDto;
import com.cos.blogapp.web.dto.LoginReqDto;

import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@Controller
public class UserController {

	private final UserRepository userRepository; 
	private final HttpSession session;
	


	@GetMapping("/login")
	public String login() {
		return "user/login"; 
	}

	@GetMapping("/loginForm")
	public String loginForm() {
		return "user/loginForm"; 
	}

	@GetMapping("/joinForm")
	public String joinForm() {
		return "user/joinForm"; 
	}
	
	@PostMapping("/login")
	public @ResponseBody String login(@Valid LoginReqDto dto, BindingResult bindingResult, Model model) {
		
		System.out.println( "로그인 에러 사이즈:"+bindingResult.getFieldErrors().size() );
		
		if(bindingResult.hasErrors()) {
			Map<String,String> errorMap = new HashMap<>();
			for( FieldError error: bindingResult.getFieldErrors()) {
				errorMap.put(error.getField(), error.getDefaultMessage());
				System.out.println("필드 :" + error.getField());
				System.out.println("메세지 :" + error.getDefaultMessage());
			}
			//model.addAttribute("errorMap", errorMap);
			return Script.back(errorMap.toString());
			
		}
		
		
		System.out.println(dto.getUsername());
		System.out.println(dto.getPassword());
		// 1.username, password 받기
		// 2. DB -> 조회
		// 3. 있을때 : session에 user의 모든 row(data) 저장 => id만 안하는 것은 마이페이지에서 session 불러오기 가능. 
		// 4. 없을때 :
		// 5. return : 메인페이지.
		
		//2. DB ->조회
		User userEntity = userRepository.mLogin(dto.getUsername(), dto.getPassword());
		
		
		
		if(userEntity == null) { //username이나 password가 틀렸을 경우
		
			return Script.back("틀림");
		} else {
			session.setAttribute("principal", userEntity);  //principal 인증주체
			return Script.href("/","로그인 성공");
		}
		
		
	}
	@PostMapping("/join")
	public @ResponseBody String join(@Valid JoinReqDto dto, BindingResult bindingResult, Model model) { //user를 안쓰고 joinreqdto 를 쓴 이유가 validation을 못써서
		//이렇게 validation으로 해서 오류가 나서 터지면 bindingresult에 자동으로 담아준다.
		//오류가 안나면 안담기고 빈채로 있다.
		
		// 1. 유효성 검사 실패 - javascript 응답 (경고창 + 페이지는 돌아가되 input 남아있도록)
		// 2. 정상 - 로그인 페이지
		
		
		//System.out.println("에러사이즈 :" + bindingResult.getFieldErrors().size()); //reflection
		
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
		
		
		userRepository.save(dto.toEntity());
		
		
		
		return Script.href("/loginForm"); // 리다이렉션(300)
	}
	
}
