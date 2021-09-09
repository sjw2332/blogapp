package com.cos.blogapp.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


//save(user) insert, update 둘 다 시행. 
// ex) PK인 id를 없이 save 하면 insert, 이미 존재하는 id(username 아님) 를 입력하고 넣으면 update
//findById(1) 한건 셀렉트
//findAll() 전체 셀렉트
// deleteMyId(1)한건 삭제



//DAO
//@Repository - 내부적으로 걸려있어서 따로 안걸어도 됨.
public interface UserRepository extends JpaRepository<User, Integer> {
	
	@Query(value = "insert into user (username, password, email) values (:username,:password,:email)",nativeQuery = true)
	void join(String username, String password, String email);
	
	@Query(value = "select * from user where username = :username and password = :password",nativeQuery = true)
	User mLogin(String username, String password );
	
}
