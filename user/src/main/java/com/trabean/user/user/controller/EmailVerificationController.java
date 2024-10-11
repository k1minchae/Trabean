package com.trabean.user.user.controller;

import com.trabean.user.user.service.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user/email")
@RequiredArgsConstructor
public class EmailVerificationController {
    private final EmailService emailService;

    @PostMapping("/send-verification-code")
    public ResponseEntity<String> sendVerificationCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        try {
            emailService.sendVerificationCode(email);
            return ResponseEntity.ok("인증 코드 전송 성공");

        } catch (MessagingException e) {
            return ResponseEntity.status(500).body("이메일 전송 실패");
        }
    }

    @PostMapping("/verify-code")
    public ResponseEntity<String> verifyCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String code = request.get("code");

        if (emailService.verifyCode(email, code)) {
            return ResponseEntity.ok("이메일 인증 성공");
        } else {
            return ResponseEntity.status(400).body("유효하지 않은 인증코드 입니다.");
        }
    }
}
