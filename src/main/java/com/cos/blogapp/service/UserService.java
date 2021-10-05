package com.cos.blogapp.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.blogapp.domain.user.User;
import com.cos.blogapp.domain.user.UserRepository;
import com.cos.blogapp.handler.ex.MyAsyncNotFoundException;
import com.cos.blogapp.util.MyAlgorithm;
import com.cos.blogapp.util.SHA;
import com.cos.blogapp.web.dto.JoinReqDto;
import com.cos.blogapp.web.dto.LoginReqDto;
import com.cos.blogapp.web.dto.UserUpdateDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
	
	private final UserRepository userRepository;
	
	
	
	
	@Transactional(rollbackFor = MyAsyncNotFoundException.class)
	public void 회원정보수정(User principal, UserUpdateDto dto) {
		
		User userEntity = userRepository.findById(principal.getId())
				.orElseThrow( ()->new MyAsyncNotFoundException("회원정보를 찾을 수 없습니다"));
		userEntity.setEmail(dto.getEmail());
		
	}
	
	
	public User 로그인(LoginReqDto dto) {
		
		return userRepository.mLogin( dto.getUsername(),
				   SHA.encrypt(dto.getPassword(), MyAlgorithm.SHA256) );
	}
	
	@Transactional(rollbackFor = MyAsyncNotFoundException.class)
	public void 회원가입(JoinReqDto dto) {
		
		String encPassword = SHA.encrypt(dto.getPassword(), MyAlgorithm.SHA256);
		dto.setPassword(encPassword);
		userRepository.save(dto.toEntity());
		
	}
	
	
}
