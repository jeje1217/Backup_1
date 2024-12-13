package com.example.food.service.adminservice;

import com.example.food.domain.Post;
import com.example.food.domain.Users;

public interface AdminService {
	
	//회원관리 
	Users getUserById(String userId);
	void deleteUser(String userId);
	
	//게시판 관리 
	Post getPostById(int postId);
	void deletePost(int postId);
	
//	//QnA 관리 
//	Question getQuestionById(int questionId);
//	List<Question> getAllQuestionSortByLatest();
//	void writeAnswer(int questionId, int answerId);
//	void deleteQuestion(int questionId);

}
