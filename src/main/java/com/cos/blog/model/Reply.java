package com.cos.blog.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;

import com.cos.blog.dto.ReplySaveRequestDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Reply {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // auto_incremnet
	private int id;
	
	@Column(nullable = false, length = 200)
	private String content;

	@ManyToOne // many: 여러개의 답변, one: 하나의 게시글
	@JoinColumn(name="boardId")
	private Board board;
	
	@ManyToOne // many: 여러개의 답변, one: 하나의 유저
	@JoinColumn(name="userId")
	private User user;
	
	@CreationTimestamp
	private Timestamp createDate;
	
//	public void update(User user, Board board, String content) {
//		setUser(user);
//		setBoard(board);
//		setContent(content);
//	}
	
}
