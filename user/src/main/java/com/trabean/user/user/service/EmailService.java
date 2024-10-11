package com.trabean.user.user.service;

import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final Map<String, String> verificationCodes = new ConcurrentHashMap<>();
    private final Map<String, Long> expirationTimes = new ConcurrentHashMap<>();

    private final long expirationMillis = 5 * 60 * 1000;

    public void sendVerificationCode(String email) throws MessagingException {
        String code = generateVerificationCode();
        verificationCodes.put(email, code);
        expirationTimes.put(email, System.currentTimeMillis() + expirationMillis);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
        helper.setTo(email);
        helper.setSubject("[trabean]인증 코드");
        helper.setText("인증코드: " + code, true);
        mailSender.send(message);
    }

    private String generateVerificationCode() {
        Random random = new Random();
        return String.format("%06d",random.nextInt(1000000));
    }

    public boolean verifyCode(String email, String code) {
        if (verificationCodes.containsKey(email)) {
            long currentTime = System.currentTimeMillis();
            if (currentTime > expirationTimes.get(email)) {
                verificationCodes.remove(email);
                expirationTimes.remove(email);
                return false;
            }
            return verificationCodes.get(email).equals(code);
        }
        return false;
    }

    @PostConstruct
    private void removeExpiredCodes() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(60*1000);
                    long currentTime = System.currentTimeMillis();
                    expirationTimes.forEach((email,expirationTime) -> {
                        if (currentTime > expirationTime) {
                            verificationCodes.remove(email);
                            expirationTimes.remove(email);
                        }
                    });
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();
    }
}
