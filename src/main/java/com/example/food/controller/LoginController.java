package com.example.food.controller;

import com.example.food.domain.Users;
import com.example.food.service.userservice.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping
public class LoginController {

    private final UserService us;

    @Autowired
    public LoginController(UserService us) {
        this.us = us;
    }

    @GetMapping("/login")
    public String login() {
        return "login/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("userId") String userId,
                        @RequestParam("password") String password,
                        Model model,
                        HttpSession session) {
        Users user = us.getUser(userId); // 유저 정보 조회
        if (user != null && user.getPassword().equals(password)) {
            session.setAttribute("loginUser", user); // 세션에 사용자 정보 저장
            return "redirect:/main/main"; // 로그인 성공 후 메인 페이지로 리다이렉트
        } else {
            model.addAttribute("error", "아이디 또는 비밀번호가 잘못되었습니다.");
            return "login/login"; // 로그인 페이지로 다시 이동
        }
    }

    // 회원가입 페이지
    @GetMapping("/join") // 수정: 회원가입 경로 추가
    public String signupForm() {
        return "login/join"; // 회원가입 페이지 (Thymeleaf 템플릿)
    }

    // 회원가입 처리
    @PostMapping("/join/process") // 추가: 회원가입 처리 경로 추가
    public String processSignup(@ModelAttribute Users user, 
                                 @RequestParam("passwordConfirm") String passwordConfirm, 
                                 RedirectAttributes redirectAttributes) {
        if (!user.getPassword().equals(passwordConfirm)) { // 추가: 비밀번호 확인 로직
            redirectAttributes.addFlashAttribute("error", "비밀번호가 일치하지 않습니다.");
            return "redirect:/join";
        }

        try {
            us.newUser(user); // 추가: UserService를 통해 회원가입 처리
            redirectAttributes.addFlashAttribute("message", "회원가입이 완료되었습니다. 로그인해주세요.");
            return "redirect:/login";
        } catch (IllegalArgumentException e) { // 추가: 예외 처리
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/join";
        }
    }

    // 기존 메서드는 변경 없음

    @GetMapping("/findId")
    public String findIdPage() {
        return "login/findId"; // 아이디 찾기 페이지 반환
    }

    @PostMapping("/findId")
    public String findId(@RequestParam("email") String email, Model model) {
        String userId = us.findUserIdByEmail(email);
        if (userId != null) {
            model.addAttribute("message", "아이디는 " + userId + " 입니다.");
        } else {
            model.addAttribute("error", "등록된 이메일이 없습니다.");
        }
        return "login/findId";
    }

    @GetMapping("/findPassword")
    public String findPasswordPage() {
        return "login/findPassword"; // 비밀번호 찾기 페이지 반환
    }

    @PostMapping("/findPassword")
    public String findPassword(@RequestParam("userId") String userId,
                               @RequestParam("email") String email,
                               @RequestParam("newPassword") String newPassword,
                               Model model) {
        boolean result = us.resetPassword(userId, email, newPassword); // 암호화 포함 비밀번호 재설정
        if (result) {
            model.addAttribute("message", "비밀번호가 변경되었습니다. 로그인해주세요.");
            return "redirect:/login/login";
        } else {
            model.addAttribute("error", "입력하신 정보가 일치하지 않습니다. 다시 입력해주세요.");
            return "login/findPassword";
        }
    }
}
