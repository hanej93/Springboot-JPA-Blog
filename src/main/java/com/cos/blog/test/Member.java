package com.cos.blog.test;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

// @Getter // 게터
// @Setter // 세터
@Data       // 게터세터 둘다
@AllArgsConstructor // 풀생성자
//@RequiredArgsConstructor // 파이널생성자
@NoArgsConstructor // 빈생성자
public class Member {
	
	private int id;
	private String username;
	private String password;
	private String email;
	
//	@Builder  // 빌더 패턴 으로 만들어줌!!
//	public Member(int id, String username, String password, String email) {
//		super();
//		this.id = id;
//		this.username = username;
//		this.password = password;
//		this.email = email;
//	}
	
	
	
	
}
