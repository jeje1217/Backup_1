package com.example.food.service.userservice;

import com.example.food.domain.Users;
import com.example.food.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

	private final UserRepository UserRepo;
	private final BCryptPasswordEncoder passwordEncoder;

	@Autowired
	public UserServiceImpl(UserRepository userRepo, BCryptPasswordEncoder passwordEncoder) {
		this.UserRepo = userRepo;
		this.passwordEncoder = passwordEncoder;
	}

	// Spring Security UserDetailsService 구현
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Users user = UserRepo.findByUserId(username); // 정확히 User ID로 조회
		if (user == null) {
			log.warn("로그인 실패: 사용자를 찾을 수 없습니다. 아이디: {}", username);
			throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
		}

		// Spring Security의 User 객체를 반환
		return User.builder().username(user.getUserId()) // 사용자 ID
				.password(user.getPassword()) // 암호화된 비밀번호
				.roles(user.getRole().name()) // ROLE_USER, ROLE_ADMIN 등
				.build();
	}

	@Override
	public void newUser(Users user) throws IllegalArgumentException {
		// 아이디 중복 체크
		if (UserRepo.existsByUserId(user.getUserId())) {
			throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
		}

		// 이메일 중복 체크
		if (UserRepo.existsByEmail(user.getEmail())) {
			throw new IllegalArgumentException("이미 등록된 이메일입니다.");
		}

		// 필수 입력값 검증
		if (user.getName() == null || user.getName().isEmpty()) {
			throw new IllegalArgumentException("이름은 필수 입력 항목입니다.");  
		}
		if (user.getPhone() == null || user.getPhone().isEmpty()) {
			throw new IllegalArgumentException("전화번호는 필수 입력 항목입니다.");  
		}
		if (user.getAddress() == null || user.getAddress().isEmpty()) {
			throw new IllegalArgumentException("주소는 필수 입력 항목입니다.");  
		}
		if (user.getBirthday() == null) {
			throw new IllegalArgumentException("생일은 필수 입력 항목입니다.");  
		}
		if (user.getHeight() <= 0) {
			throw new IllegalArgumentException("키는 0보다 커야 합니다.");  
		}
		if (user.getWeight() <= 0) {
			throw new IllegalArgumentException("몸무게는 0보다 커야 합니다.");  
		}

		// 비밀번호 암호화
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);

		// 기본 역할 설정
		if (user.getRole() == null) {
			user.setRole(Users.Role.ROLE_USER);
		}

		// DB에 저장
		UserRepo.save(user);
		log.info("새로운 사용자가 등록되었습니다: {}", user.getUserId());
	}

	@Override
	public Users getUser(String userId) {
		return UserRepo.findByUserId(userId);
	}

	@Override
	public void changeUserRole(Users user, String newRole) {
		Users changeRole = UserRepo.findByUserId(user.getUserId());
		if (changeRole == null) {
			throw new IllegalArgumentException("아이디를 찾을 수 없습니다: " + user.getUserId());
		}
		Users.Role roleEnum = Users.Role.valueOf(newRole);
		if (roleEnum.equals(changeRole.getRole())) {
			throw new IllegalArgumentException("기존과 동일한 설정입니다.");
		}
		changeRole.setRole(roleEnum);
		UserRepo.save(changeRole);
	}

	@Override
	public void deleteUser(Users user) {
		UserRepo.delete(user);
	}

	@Override
	public void changeUser(Users updatedUser) {
		UserRepo.save(updatedUser);
	}

	@Override
	public String emailCheck(String email) {
		Users user = UserRepo.findByEmail(email);
		return user != null ? user.getEmail() : null;
	}

	@Override
	public String findUserIdByEmail(String email) {
		Users user = UserRepo.findByEmail(email);
		if (user != null) {
			log.info("아이디 찾기 성공: {}", user.getUserId());
			return user.getUserId();
		}
		log.warn("아이디 찾기 실패: 이메일이 존재하지 않음 - {}", email);
		return null;
	}

	@Override
	public boolean resetPassword(String userId, String email, String newPassword) {
		Users user = UserRepo.findByUserId(userId);
		if (user != null && user.getEmail().equals(email)) {
			String encodedPassword = passwordEncoder.encode(newPassword);
			user.setPassword(encodedPassword);
			UserRepo.save(user);
			log.info("비밀번호 재설정 성공: {}", userId);
			return true;
		}
		log.warn("비밀번호 재설정 실패: 정보 불일치 - {}, {}", userId, email);
		return false;
	}
}
