package com.cos.blog.repsitory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.cos.blog.model.Reply;

public interface ReplyRepository extends JpaRepository<Reply, Integer>{
	
	@Modifying // 그냥 쿼리를 날리면 셀렉트로 인식이되어서 리절트셋으로 리턴하려한다. 그리고 insert, update는 int를 리턴해줌
	@Query(value = "INSERT INTO reply(userId, boardId, content, createDate) VALUES(?1, ?2, ?3, now())", nativeQuery = true)
	int mSave(int userId, int boardId, String content);
}
