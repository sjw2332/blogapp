package com.cos.blogapp.domain.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 테이블 모델
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class User {  
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id; //PK (자동증가 번호)
	@Column(nullable = false, length = 20, unique = true)
	private String username; // 아이디
	@Column(nullable = false, length = 70)
	private String password;
	@Column(nullable = false, length = 50)
	private String email;
}
