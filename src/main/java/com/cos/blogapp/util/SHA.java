package com.cos.blogapp.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA {
	
	public static String encrypt(String rawPassword, MyAlgorithm algorithm) {
	
		// 1. sha256 함수를 가진 클래스 객체 가져오기
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance(algorithm.getType());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		StringBuilder sb = new StringBuilder();
		
		// 2. 비밀변호 1234 -> sha256 던지기 (1, 2 ,3,4 를 각각 바이트로 바꿈)
		md.update(rawPassword.getBytes());
		

		//헥사코드로 변경
		for(Byte b:md.digest()) {
			sb.append(String.format("%02x", b));
		}
		System.out.println(sb.toString());	
		
		//해쉬 : 위와 같은 암호화 기술 ,  --고정길이로 변경됨, 복호화가 안됨(암호화만 됨)
		//
		return sb.toString();
	}
	
}
