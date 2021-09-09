package com.cos.blogapp.domain.board;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import com.cos.blogapp.domain.user.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Board {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id; //PK
	private String title;
	@Lob
	private String content;
	
	//one은 user , 한명의 유저가 여러 게시글을 쓸 것이므로
	@JoinColumn(name="userId")
	@ManyToOne(fetch = FetchType.EAGER)
	private User user;
}
