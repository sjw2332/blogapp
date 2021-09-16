package com.cos.blogapp.test;

// 1.8 람다식
// 1. 함수를 넘기는게 목적
// 2. Interface에 함수가 무조건 하나여야 함.
// 3. 쓰면 코드가 간결해지고, 타입을 몰라도 됨.

interface MySupplier{
	void get();
}


public class LamdaTest {
	
	static void start(MySupplier s) {
		s.get();
	}
	

	public static void main(String[] args) {
		
		//start(get); 함수 -> 1급객체가 아님 그래서 이대로는 에러가 뜬다.
		
		//MySupplier 가 interface 이므로 익명클래스를 넣어준다. (new + 자동완성)
		
		start( () -> { System.out.println("get함수 호출됨"); } );
	}
}
