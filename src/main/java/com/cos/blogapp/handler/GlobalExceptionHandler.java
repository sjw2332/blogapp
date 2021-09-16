package com.cos.blogapp.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cos.blogapp.handler.ex.MyNotFoundException;
import com.cos.blogapp.util.Script;

//@ControllerAdvice = 1.익셉션 핸들링, 2.@Controller 역할도 함
@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(value = MyNotFoundException.class)
	public @ResponseBody String error1(MyNotFoundException e) {
		//System.out.println("NoSuchElementException 터짐 ㄷㄷㄷ");
		System.out.println("오류당 : "+e.getMessage());
		//void라서 화면에 뭔가 띄우진 않음.
		//return "안녕";  -> 안녕.jsp
		return Script.href("/", "해당글을 찾을 수 없습니다.");
	}
}
