package com.example.food.controller;

import com.example.food.service.*;
import com.example.food.service.adminservice.AdminService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // 회원 관리: 회원 아이디로 조회
    @GetMapping("/users")
    public String getMembers(@RequestParam(value = "userId", required = false) String userId, Model model) {
        if (userId != null) {
            model.addAttribute("member", adminService.getUserById(userId));
        }
        return "admin/users";
    }

    // 회원 삭제
    @PostMapping("/members/delete/{userId}")
    public String deleteMember(@PathVariable String userId) {
        adminService.deleteUser(userId);
        return "redirect:/admin/users";
    }

    // 게시판 관리: 게시글 번호로 조회
    @GetMapping("/post")
    public String getBoard(@RequestParam(value = "postId", required = false) Integer postId, Model model) {
        if (postId != null) {
            model.addAttribute("post", adminService.getPostById(postId));
        }
        return "admin/post";
    }

    // 게시글 삭제
    @PostMapping("/board/delete/{postId}")
    public String deletePost(@PathVariable int postId) {
        adminService.deletePost(postId);
        return "redirect:/admin/post";
    }

//    // QnA 관리: 질문 번호로 조회
//    @GetMapping("/qna")
//    public String getQnA(@RequestParam(value = "questionId", required = false) Long questionId, Model model) {
//        if (questionId != null) {
//            model.addAttribute("question", adminService.getQuestionById(questionId));
//        } else {
//            model.addAttribute("questions", adminService.getAllQuestionsSortedByLatest());
//        }
//        return "admin/qna";
//    }
//
//    // QnA 답변 쓰기
//    @PostMapping("/qna/answer/{questionId}")
//    public String writeAnswer(@PathVariable Long questionId, @RequestParam String answerContent) {
//        adminService.writeAnswer(questionId, answerContent);
//        return "redirect:/admin/qna";
//    }
//
//    // QnA 질문 삭제
//    @PostMapping("/qna/delete/{questionId}")
//    public String deleteQuestion(@PathVariable Long questionId) {
//        adminService.deleteQuestion(questionId);
//        return "redirect:/admin/qna";
//    }
}
