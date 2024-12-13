package com.example.food.service.adminservice;

import com.example.food.domain.*;
import com.example.food.repository.*;
import com.example.food.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
//    private final QuestionRepository questionRepository;

    @Autowired
    public AdminServiceImpl(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
//        this.questionRepository = questionRepository;
    }

    @Override
    public Users getUserById(String userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Override
    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public Post getPostById(int postId) {
        return postRepository.findById(postId).orElse(null);
    }

    @Override
    public void deletePost(int postId) {
        postRepository.deleteById(postId);
    }

//    @Override
//    public Question getQuestionById(int questionId) {
//        return questionRepository.findById(questionId).orElse(null);
//    }
//
//    @Override
//    public List<Question> getAllQuestionsSortedByLatest() {
//        return questionRepository.findAllByOrderByCreatedAtDesc();
//    }
//
//    @Override
//    public void writeAnswer(Long questionId, String answerContent) {
//        Question question = questionRepository.findById(questionId).orElseThrow(() -> new IllegalArgumentException("질문을 찾을 수 없습니다."));
//        Answer answer = new Answer();
//        answer.setQuestion(question);
//        answer.setContent(answerContent);
//        answer.setCreatedAt(LocalDateTime.now());
//        question.getAnswers().add(answer);
//        questionRepository.save(question);
//    }
//
//    @Override
//    public void deleteQuestion(Long questionId) {
//        questionRepository.deleteById(questionId);
//    }
}
