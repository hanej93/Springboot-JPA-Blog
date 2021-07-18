package com.cos.blog.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.repsitory.UserRepository;

// 스프링이 컴포넌트 스캔을 통해서 Bean에 등록ㅇ르 해줌 Ioc를 해준다.
@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Transactional(readOnly = true)
	public User 회원찾기(String username) {
		User user = userRepository.findByUsername(username)
				// 찾지 못했을 경우 객체를 리턴하는 함수
				.orElseGet(()->{
					return new User();
				});
		return user;
	}
	
	
	
	@Transactional
	public void 회원가입(User user) {
		String rawPassword = user.getPassword(); // 1234 원문
		String encPassword = encoder.encode(rawPassword); // 해쉬
		user.setPassword(encPassword);
		user.setRole(RoleType.USER);
		userRepository.save(user);
	}

	@Transactional
	public void 회원수정(User user) {
		// 수정시에는 영속석 컨텍스트 User 오브젝트를 영속화시키고, 영속화된 User 오브젝트를 수정
		// select를 해서 User오브젝트를 DB로 부터 가져오는 이유는 영속화를 하기 위해서!!
		// 영속화된 오브젝트를 변경하면 자동으로 DB에 update문을 날려주거든요.
		User persistence = userRepository.findById(user.getId())
				.orElseThrow(()->{
					return new IllegalArgumentException("회원 찾기 실패");
				});
		
		// Validate 체크
		if(persistence.getOauth() == null || persistence.getOauth().equals("")) {
			String rawPassword = user.getPassword();
			String encPassword = encoder.encode(rawPassword);
			persistence.setPassword(encPassword);
			persistence.setEmail(user.getEmail());
		}
		
		
		
		
		
		// 회원수정 함수 종료시 = 서비스 종료 = 트랜잭션 통료 = commit이 자동으로 됩니다.
		// 영속화된 persistence 객체의 변화가 감지디면 더티체킹이 되어 update문을 날려줌
	}

	/*
	 * @Transactional(readOnly = true) // Select할 때 트랜젝션 시작, 서비스 종료시에 트랜젝션 종료 (정합성)
	 * public User 로그인(User user) { return
	 * userRepository.findByUsernameAndPassword(user.getUsername(),
	 * user.getPassword()); }
	 */
}
