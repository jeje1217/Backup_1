package com.example.food.service.userservice;

import com.example.food.domain.Users;

public interface UserService {

    //  유저 정보 가져오기
    Users getUser(String userId);

    //  특정 유저에 대한 권한 변경
    void changeUserRole(Users user, String newRole);

    //회원가입 메서드
    void newUser(Users user) throws IllegalArgumentException;

    //  회원 탈퇴
    void deleteUser(Users user);

    //  회원 정보 수정
    void changeUser(Users user);

    //회원가입 시 이메일로 등록여부 확인 
	String emailCheck(String email);

	//이메일로 아이디 찾기
	String findUserIdByEmail(String email);

	//비밀번호 재설정 
	boolean resetPassword(String userId, String email, String newPassword);
	
	
	
    
}
