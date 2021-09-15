package com.cos.blogapp.util;

import lombok.Getter;

//열거형 (카테고리 정할 떄, 범주가 정해져 있을때) -> 실수하지 않기 위해 사용
@Getter
public enum MyAlgorithm {
	SHA256("SHA-256"), SHA512("SHA-512");
	
	private String type;
	
	private MyAlgorithm(String type) {
		this.type = type;
	}
	
}
