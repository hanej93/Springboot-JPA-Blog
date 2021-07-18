package com.cos.blog.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.blog.dto.ReplySaveRequestDto;
import com.cos.blog.model.Board;
import com.cos.blog.model.Reply;
import com.cos.blog.model.User;
import com.cos.blog.repsitory.BoardRepository;
import com.cos.blog.repsitory.ReplyRepository;
import com.cos.blog.repsitory.UserRepository;

import lombok.RequiredArgsConstructor;

// 스프링이 컴포넌트 스캔을 통해서 Bean에 등록ㅇ르 해줌 Ioc를 해준다.
@Service
@RequiredArgsConstructor
public class BoardService {
	

	private final BoardRepository boardRepository;
	
	private final ReplyRepository replyRepository;
	
	
	@Transactional
	public void 글쓰기(Board board, User user) { // title, content
		board.setCount(0);
		board.setUser(user);
		boardRepository.save(board);
	}

	@Transactional(readOnly = true)
	public Page<Board> 글목록(Pageable pageable) {
		return boardRepository.findAll(pageable);
	}


	@Transactional(readOnly = true)
	public Board 글상세보기(int id) {
		return boardRepository.findById(id)
				.orElseThrow(()->{
					return new IllegalArgumentException("글 상세보기 실패: 아이디를 찾을 수 없습니다.");
				});
	}


	@Transactional
	public void 글삭제하기(int id) {
		boardRepository.deleteById(id);
	}

	@Transactional
	public void 글수정하기(int id, Board requestBoard) {
		// 영속화 완료
		Board board = boardRepository.findById(id)
				.orElseThrow(()->{
					return new IllegalArgumentException("글 찾기 실패: 아이디를 찾을 수 없습니다.");
				});
		board.setTitle(requestBoard.getTitle());
		board.setContent(requestBoard.getContent());
		// 해당 함수로 종료시(Service가 종료될 때) 트랜잭션이 종료됩니다.
		// 이때 더티체킹이 일어나면서 자동 업데이트가 db flush
	}

	@Transactional
	public void 댓글쓰기(ReplySaveRequestDto replySaveRequestDto) {
		/*
		User user = userRepository.findById(replySaveRequestDto.getUserId())
				.orElseThrow(()->{
					return new IllegalArgumentException("댓글 쓰기 실패: 유저 id를 찾을 수 없습니다.");
				});
		
		Board board = boardRepository.findById(replySaveRequestDto.getBoardId())
				.orElseThrow(()->{
					return new IllegalArgumentException("댓글 쓰기 실패: 게시글 id를 찾을 수 없습니다.");
				});
		
		Reply reply = Reply.builder()
				.user(user)
				.board(board)
				.content(replySaveRequestDto.getContent())
				.build();
		
		replyRepository.save(reply);
		*/
		
		int result = replyRepository.mSave(replySaveRequestDto.getUserId(), replySaveRequestDto.getBoardId(), replySaveRequestDto.getContent());
		System.out.println("영향받은 행의 갯수: " + result);
	}

	@Transactional
	public void 댓글삭제(int replyId) {
		replyRepository.deleteById(replyId);
	}

	/*
	 * @Transactional(readOnly = true) // Select할 때 트랜젝션 시작, 서비스 종료시에 트랜젝션 종료 (정합성)
	 * public User 로그인(User user) { return
	 * userRepository.findByUsernameAndPassword(user.getUsername(),
	 * user.getPassword()); }
	 */
}
