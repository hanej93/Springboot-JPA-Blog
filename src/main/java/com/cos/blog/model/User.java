package com.cos.blog.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


// ORM -> Java(다른언어) Object  -> 테이블로 매핑해주는 기술

@Data // 겟터셋터
@NoArgsConstructor // 빈생성자
@AllArgsConstructor // 풀생성자
@Builder // 빌더 패턴
@Entity // User 클래스가 MySQL에 테이블이 생성된다.
//@DynamicInsert // 디폴트가 있는 값에 널이 들어가면 디폴트값으로 넣어주는 설정, null인 항목은 인서트시키지않음
public class User {

	@Id // Primary key
	// 프로젝트에서 연결된 DB의 넘버링 전략을 따라간다.
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id; //시퀀스, auto_increment
	
	@Column(nullable = false, length = 100, unique = true)
	private String username; // 아이디
	
	
	@Column(nullable = false, length = 100) // 123456 -> 해쉬(비밀번호 암호화)
	private String password;
	
	@Column(nullable = false, length = 50)
	private String email;
	
	//@ColumnDefault("'user'")
	// DB는RoleType이라는 것이 없음 따라서 String이라고 알려주는 설정
	@Enumerated(EnumType.STRING)
	private RoleType role; // Enum을 쓰는게 좋다. // admin, user, manager
	
	
	private String oauth; // kakao, google, ...
	
	@CreationTimestamp // 시간이 자동 입력
	private Timestamp createDate;
	
}
