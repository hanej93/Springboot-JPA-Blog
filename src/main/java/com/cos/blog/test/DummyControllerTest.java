package com.cos.blog.test;

import java.util.List;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.repsitory.UserRepository;


// save함수는 id를 전달하지 않으면 insert를 해주고
// save함수는 id를 전달하면 해당 id에 대한 데이터가 있으면 update를 해주고
// save함수는 id를 전달하면 해당 id에 대한 데이터가 없으면 insert를 함
// html 파일이 아니라 data를 리턴해주는 controller = RestController
@RestController
public class DummyControllerTest {
	
	@Autowired // 의존성 주입(DI)
	private UserRepository userRepository;
	
	@DeleteMapping("/dummy/user/{id}")
	public String delete(@PathVariable int id) {
		try {
			userRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			return "삭제에 실패하였습니다. 해당 id는 DB에 없습니다.";
		}
		
		
		return "삭제되었습니다. id: "+ id;
	}
	
	
	// eamil, password
	// @RequestBody : json 데이터로 받기 위함
	// -> json 데이터를 요청 -> Java Object(MessageConverter의 Jackson 라이브러리가 변환해서 받아줘요.)
	@Transactional // 함수 종료시에 자동 commit 됨
	@PutMapping("/dummy/user/{id}")
	public User updateUser(@PathVariable int id, @RequestBody User requestUser) {
		System.out.println("id: " + id);
		System.out.println("passowrd: " +requestUser.getPassword() );
		System.out.println("passowrd: " +requestUser.getEmail() );
		
		User user = userRepository.findById(id).orElseThrow(()->{
			return new IllegalArgumentException("수정에 실패하였습니다.");
		});
		user.setPassword(requestUser.getPassword());
		user.setEmail(requestUser.getEmail());
		
		// userRepository.save(user);
		
		// 더티 체킹
		return user;
	}
	
	
	
	// http://localhost:8000/blog/dummy/user
	@GetMapping("/dummy/users")
	public List<User> list() {
		return userRepository.findAll();
	}
	
	// 한페이지당 2건의 데이터를 리턴받아 볼 예정
	@GetMapping("/dummy/user")
	public List<User> pageList(@PageableDefault(size = 2, sort = "id", direction = Direction.DESC) Pageable pageable){
		Page<User> pagingUser = userRepository.findAll(pageable);
		
//		if(pagingUser.isFirst()) {
//			
//		}
		List<User> users = pagingUser.getContent();
		return users;
	}
	
	
	// {id} 주소로 파라미터를 전달 받을 수 있음
	// http://localhost:8000/blog/dummy/user/3
	@GetMapping("/dummy/user/{id}")
	public User detail(@PathVariable int id) {
		
		
		// user/4 를 찾으면 내가 데이터베이스에서 못 찾아오게 되면 user가 null이 될 것아냐?
		// 그럼 return null 이 리턴이 되자나.. 그럼 프로그램에 문제가 있지 않겠니?
		// Optional로 너의 User 객체를 감싸서 가져올테니 null인지 아닌지 판단해서 리턴해
		
		// null이 아니면 객체를 넣어줘
		
		//User user = userRepository.findById(id).get();
		
		// 람다식 어떤 타입을 리턴해야 되는지 생각안해도 됨
//		User user = userRepository.findById(id).orElseThrow(()->{
//			return new IllegalArgumentException("해당 사용자는 없습니다");
//		});
				
		User user = userRepository.findById(id).orElseThrow(new Supplier<IllegalArgumentException>() {
			@Override
			public IllegalArgumentException get() {
				// TODO Auto-generated method stub
				return new IllegalArgumentException("해당 유저는 없습니다. id: " + id);
			}
		
		});
		
		// 요청: 웹브라우저
		// user 객체 = 자바 오브젝트
		// 변환(웹브라우저가 이해할 수 있는 데이터) -> json(Gson 라이브러리)
		// 스프링부트 = MessageConverter라는 애가 응답시에 자동 작동
		// 만약에 자바 오브젝트를 리턴하게 되면 MessageConverter가 Jackson 라이브러리를 호출해서
		// user 오브젝트를 json으로 변환해서 브라우저에게 던져줍니다.
		return user;
		
	}
	
	
	
	// http://localhost:8000/blog/dummy/join (요청)
	// http의 body에 username, password, eamil 데이터를 가지고 (요청)
	@PostMapping("/dummy/join")
	// 변수명을 정확히 적으면 @RequestParam("username") 이렇게 할 필요 없이 적어줘도됨
	// key = value 형태로 받아줌(약속된 규칙)
	// String username, String password, String email -> User user 오브젝트로 받을 수 있음
	public String join(User user) { 
		System.out.println("id" + user.getId());
		System.out.println("username" + user.getUsername());
		System.out.println("password" + user.getPassword());
		System.out.println("email" + user.getEmail());
		System.out.println("data" + user.getCreateDate());
		System.out.println("role" + user.getRole());
		
		// 롤타입 USER를 넣음
		user.setRole(RoleType.USER);
		// .save(객체) 객체에 담긴 정보를 인서트 시킴
		userRepository.save(user);
		
		return "회원가입이 완료되었습니다.";
	}
}
