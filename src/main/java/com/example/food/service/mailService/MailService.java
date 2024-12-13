package com.example.food.service.mailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;
    private static final int CODE_EXPIRATION_MINUTES = 5; // 인증번호 유효 시간
    private final Map<String, VerificationCode> verificationCodes = new HashMap<>(); // 이메일별 인증번호 저장

    // 이메일 전송
    public void sendMail(String email) throws MessagingException {
        int code = generateCode();
        verificationCodes.put(email, new VerificationCode(code, LocalDateTime.now()));

        MimeMessage message = javaMailSender.createMimeMessage();
        message.setSubject("이메일 인증");
        message.setText("<h1>인증번호: " + code + "</h1>", "UTF-8", "html");
        message.setRecipients(MimeMessage.RecipientType.TO, email);

        javaMailSender.send(message);
    }

    // 인증번호 확인
    public boolean verifyCode(String email, String code) {
        VerificationCode storedCode = verificationCodes.get(email);
        if (storedCode == null) return false;

        // 코드가 만료되었는지 확인
        if (storedCode.getGeneratedAt().plusMinutes(CODE_EXPIRATION_MINUTES).isBefore(LocalDateTime.now())) {
            verificationCodes.remove(email);
            return false;
        }

        return String.valueOf(storedCode.getCode()).equals(code);
    }

    // 인증번호 생성
    private int generateCode() {
        return (int) (Math.random() * 900000) + 100000; // 6자리 인증번호 생성
    }

    // 내부 클래스: 인증번호와 생성 시간 저장
    private static class VerificationCode {
        private final int code;
        private final LocalDateTime generatedAt;

        public VerificationCode(int code, LocalDateTime generatedAt) {
            this.code = code;
            this.generatedAt = generatedAt;
        }

        public int getCode() {
            return code;
        }

        public LocalDateTime getGeneratedAt() {
            return generatedAt;
        }
    }
}
