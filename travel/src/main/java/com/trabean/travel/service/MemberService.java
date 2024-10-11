package com.trabean.travel.service;

import com.trabean.travel.dto.request.InvitaionRequestDto;
import com.trabean.travel.entity.Invitation;
import com.trabean.travel.entity.KrwTravelAccount;
import com.trabean.travel.repository.InvitationRepository;
import com.trabean.travel.repository.KrwTravelAccountRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.sql.Timestamp;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final JavaMailSender mailSender;

    private final InvitationRepository invitationRepository;
    private final KrwTravelAccountRepository krwTravelAccountRepository;

    @Transactional
    public void invite(InvitaionRequestDto invitaionRequestDto) {
        Long accountId = invitaionRequestDto.getAccountId();
        KrwTravelAccount account = krwTravelAccountRepository.findByAccountId(accountId);

        String email = invitaionRequestDto.getEmail();

        Invitation invitation = new Invitation();
        invitation.setEmail(email);
        invitation.setAccepted(false);
        invitation.setInviteDate(new Timestamp(System.currentTimeMillis()));
        invitation.setAccount(account);

        try {
            sendMail(email);
        } catch (MessagingException e) {
            throw new RuntimeException("메일 전송 중 문제가 발생했습니다.", e);
        }

        invitationRepository.save(invitation);
    }

    @Async
    public void sendMail(String to) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        String subject = "[Trabean] 여행통장에 초대되었습니다.";
        String body = "여행통장에 초대되었습니다.";

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setTo(to);                // 수신자
        mimeMessageHelper.setSubject(subject);      // 제목
        mimeMessageHelper.setText(body, true); // 본문

        mailSender.send(mimeMessage);
    }

}
