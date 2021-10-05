package com.cos.blogapp.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cos.blogapp.handler.ex.MyAsyncNotFoundException;
import com.cos.blogapp.handler.ex.MyNotFoundException;
import com.cos.blogapp.util.Script;
import com.cos.blogapp.web.dto.CMRespDto;

//@ControllerAdvice = 1.익셉션 핸들링, 2.@Controller 역할도 함
@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(value = MyNotFoundException.class)
	public @ResponseBody String error1(MyNotFoundException e) {
		//System.out.println("NoSuchElementException 터짐 ㄷㄷㄷ");
		System.out.println("오류당 : "+e.getMessage());
		//void라서 화면에 뭔가 띄우진 않음.
		//return "안녕";  -> 안녕.jsp
		return Script.href("/", e.getMessage());
	}

	@ExceptionHandler(value = MyAsyncNotFoundException.class)
	public @ResponseBody CMRespDto error2(MyAsyncNotFoundException e) {
		System.out.println("오류당 : "+e.getMessage());
		return new CMRespDto<>(-1,e.getMessage(),null);
	}
	
	
}
