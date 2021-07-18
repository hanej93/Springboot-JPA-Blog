package com.cos.blog.repsitory;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cos.blog.model.User;

// jsp에서의 DAO
// 자동으로 bean등록이 된다.
// @Repository 생햑이 가능하다.
// User테이블을 관리하며 프라이머리키는 integer이다
public interface UserRepository extends JpaRepository<User, Integer>{
	//SELECT * FROM user WHERE username= 1?;
	Optional<User> findByUsername(String username);
	
	
	
	//JPA Naming 쿼리
	// SELECT * FROM user WHERE username = ?1 AND password = ?2;
	//User findByUsernameAndPassword(String username, String password);
	
	//@Query(value = "SELECT * FROM user WHERE username = ?1 AND password = ?2",nativeQuery = true)
	//User login(String username, String password);
}
