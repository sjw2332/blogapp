package com.cos.blogapp.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cos.blogapp.domain.user.User;
import com.cos.blogapp.domain.user.UserRepository;
import com.cos.blogapp.handler.ex.MyAsyncNotFoundException;
import com.cos.blogapp.service.UserService;
import com.cos.blogapp.util.MyAlgorithm;
import com.cos.blogapp.util.SHA;
import com.cos.blogapp.util.Script;
import com.cos.blogapp.web.dto.CMRespDto;
import com.cos.blogapp.web.dto.JoinReqDto;
import com.cos.blogapp.web.dto.LoginReqDto;
import com.cos.blogapp.web.dto.UserUpdateDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class UserController {

	private final UserRepository userRepository;
	private final UserService userService;
	private final HttpSession session;
	
	//회원정보 수정
	@PutMapping("/user/{id}")
	public @ResponseBody CMRespDto<String> update(@PathVariable int id, @Valid @RequestBody UserUpdateDto dto, BindingResult bindingResult) {
		//유효성
		User principal = (User) session.getAttribute("principal");
	
		if(bindingResult.hasErrors()) {
			Map<String, String> errorMap = new HashMap<>();
			for(FieldError error : bindingResult.getFieldErrors()) {
				errorMap.put(error.getField(), error.getDefaultMessage());
			}
			throw new MyAsyncNotFoundException(errorMap.toString());
		}
		//인증
		if (principal == null) {
			throw new MyAsyncNotFoundException("인증되지 않았습니다.");
		}
		//권한
		if(principal.getId() != id) {
			throw new MyAsyncNotFoundException("회원정보를 수정할 권한이 없습니다.");
		}
		
		userService.회원정보수정(principal, dto);
		//핵심로직
		principal.setEmail(dto.getEmail());
		session.setAttribute("principal", principal);
		
		return new CMRespDto<>(1,"성공",null);
	}
	
	
	
	
	
/*	@PutMapping("/user/{id}")
	public @ResponseBody CMRespDto<String> update(@PathVariable int id, @Valid @RequestBody JoinReqDto dto, BindingResult bindingResult ){
		
		User principal = (User) session.getAttribute("principal");
		User user = dto.toEntity(principal);
		user.setId(id);
		
		String encPassword =  SHA.encrypt(dto.getPassword(), MyAlgorithm.SHA256);
		user.setPassword(encPassword);
		//System.out.println(encPassword);
		
		userRepository.save(user);
		
		return new CMRespDto<>(1,"업데이트 성공",null);
	}
*/	
	
	//회원정보
	@GetMapping("/user/{id}")
	public String userInfo(@PathVariable int id) {
		// 기본은 userRepository.findById(id) 디비에서 가져와야 함.
		// 편법은 세션값을 가져올 수도 있다.
		
		return "user/updateForm";
	}
	
	//로그아웃
	@GetMapping("/logout")
	public String logout() {
		session.invalidate(); // 세션 무효화 (jsessionId에 있는 값을 비우는 것)
		return "redirect:/"; // 게시글 목록 화면에 데이터가 있을까요?
	}
	
	//로그인화면
	@GetMapping("/loginForm")
	public String loginForm() {
		return "user/loginForm";
	}
	
	//회원가입화면
	@GetMapping("/joinForm")
	public String joinForm() {
		return "user/joinForm";
	}
	
	//로그인
	@PostMapping("/login")
	public @ResponseBody String login(@Valid LoginReqDto dto, BindingResult bindingResult) {
		
		if(bindingResult.hasErrors()) {
			Map<String, String> errorMap = new HashMap<>();
			for(FieldError error : bindingResult.getFieldErrors()) {
				errorMap.put(error.getField(), error.getDefaultMessage());
			}
			return Script.back(errorMap.toString());
		}
		
		
		   User userEntity = userService.로그인(dto);
		
		
		
		if(userEntity == null) { // username, password 잘못 기입
			return Script.back("아이디 혹은 비밀번호를 잘못 입력하였습니다.");
		}else {
			//세션 날라가는 조건 : 1. session.invalidate(), 2. 브라우저를 닫으면 날라감
			session.setAttribute("principal", userEntity);
			return Script.href("/", "로그인 성공");
		}
	}
	
	
	//회원가입
	@PostMapping("/join")
	public @ResponseBody String join(@Valid JoinReqDto dto, BindingResult bindingResult) { // username=love&password=1234&email=love@nate.com
		
		if(bindingResult.hasErrors()) {
			Map<String, String> errorMap = new HashMap<>();
			for(FieldError error : bindingResult.getFieldErrors()) {
				errorMap.put(error.getField(), error.getDefaultMessage());
			}
			return Script.back(errorMap.toString());
		}
		
		userService.회원가입(dto);
		
		return Script.href("/loginForm","회원가입이 성공적으로 완료되었습니다."); // 리다이렉션 (300)
	}
	
}



