package com.example.food.controller;

import com.example.food.service.mailService.MailService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/mail")
public class MailController {

    private final MailService mailService;

    public MailController(MailService mailService) {
        this.mailService = mailService;
    }

    // 이메일 인증번호 전송
    @PostMapping("/send")
    public ResponseEntity<?> sendMail(@RequestParam String email) {
        try {
            mailService.sendMail(email); // 이메일 전송
            return ResponseEntity.ok(Map.of("success", true, "message", "인증번호가 발송되었습니다."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "error", e.getMessage()));
        }
    }

    // 인증번호 확인
    @GetMapping("/verify")
    public ResponseEntity<?> verifyCode(@RequestParam String email, @RequestParam String code) {
        boolean isVerified = mailService.verifyCode(email, code);
        if (isVerified) {
            return ResponseEntity.ok(Map.of("success", true, "message", "인증이 완료되었습니다."));
        } else {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "인증번호가 일치하지 않습니다."));
        }
    }
}
