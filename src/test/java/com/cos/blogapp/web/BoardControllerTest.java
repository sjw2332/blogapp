package com.cos.blogapp.web;

import org.junit.jupiter.api.Test;

import com.cos.blogapp.domain.board.Board;

public class BoardControllerTest {
	
	@Test
	public void 익셉션테스트() {
		try {
			Board b = null;
			System.out.println(b.getContent());
		} catch (Exception e) {
			System.out.println("오류났당");
			System.out.println(e.getMessage());
		
			//throw new Exception();
		}
	}
	
	
	@Test
	public void 익셉션테스트2() throws Exception {
		//method의 에러로 발생하기 때문에 dispatcher로 throw한다.
		throw new Exception();
	}
	
	
	
}
