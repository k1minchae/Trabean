package com.trabean.travel.service;

import com.trabean.interceptor.UserHeaderInterceptor;
import com.trabean.travel.callApi.client.AccountClient;
import com.trabean.travel.callApi.client.NotificationClient;
import com.trabean.travel.callApi.client.UserClient;
import com.trabean.travel.callApi.dto.request.MemberJoinApiRequestDto;
import com.trabean.travel.callApi.dto.request.MemberRoleUpdateApiRequestDto;
import com.trabean.travel.callApi.dto.request.NotificationApiRequestDto;
import com.trabean.travel.dto.request.InvitaionRequestDto;
import com.trabean.travel.dto.request.MemberJoinRequestDto;
import com.trabean.travel.dto.request.MemberRoleChangeRequestDto;
import com.trabean.travel.entity.ForeignTravelAccount;
import com.trabean.travel.entity.Invitation;
import com.trabean.travel.entity.KrwTravelAccount;
import com.trabean.travel.repository.InvitationRepository;
import com.trabean.travel.repository.KrwTravelAccountRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
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

    private final AccountClient accountClient;
    private final UserClient userClient;
    private final NotificationClient notificationClient;

    @Transactional
    public void invite(InvitaionRequestDto invitaionRequestDto) {
        Long accountId = invitaionRequestDto.getAccountId();
        String email = invitaionRequestDto.getEmail();

        // 메일발송
        try {
            sendMail(accountId, email);
        } catch (MessagingException e) {
            throw new RuntimeException("메일 전송 중 문제가 발생했습니다.", e);
        }

        // 우리 회원이라면 알림을 추가로 발송
        Long receiverId = userClient.isMember(email);

        if(receiverId != -1) {
            List<Long> receiverList = new ArrayList<>();
            receiverList.add(receiverId);

            NotificationApiRequestDto notificationApiRequestDto = NotificationApiRequestDto.builder()
                    .senderId(UserHeaderInterceptor.userId.get())
                    .receiverId(receiverList)
                    .accountId(accountId)
                    .notificationType("INVITE")
                    .build();

            notificationClient.postNotification(notificationApiRequestDto);
        }

        // Invitation DB에 저장
        KrwTravelAccount account = krwTravelAccountRepository.findByAccountId(accountId);

        Invitation invitation = Invitation.builder()
                .email(email)
                .isAccepted(false)
                .inviteDate(new Timestamp(System.currentTimeMillis()))
                .account(account)
                .build();

        invitationRepository.save(invitation);
    }

    @Async
    public void sendMail(Long accountId, String email) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        String subject = "[Trabean] 여행통장에 초대되었습니다.";
        String url = "https://j11a604.p.ssafy.io/accounts/travel/domestic/" + accountId + "/invite?email=" + email;
        String body = "<p>여행통장에 초대되었습니다.</p>"
                + "<a href='" + url + "' style='display: inline-block; padding: 10px 20px; font-size: 16px; color: #ffffff; background-color: #28a745; text-decoration: none; border-radius: 5px;'>"
                + "여행통장 보기</a>";

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setTo(email);                  // 수신자
        mimeMessageHelper.setSubject(subject);           // 제목
        mimeMessageHelper.setText(body, true);      // HTML 본문

        mailSender.send(mimeMessage);
    }


    @Transactional
    public String join(MemberJoinRequestDto memberJoinRequestDto) {
        Long userId = UserHeaderInterceptor.userId.get();
        String email = userClient.getUserEmail(userId).getUserEmail();

        Long krwAccountId = memberJoinRequestDto.getAccountId();
        KrwTravelAccount krwTravelAccount = krwTravelAccountRepository.findByAccountId(krwAccountId);
        Invitation invitation = invitationRepository.findByEmailAndAccount(email, krwTravelAccount);

        if(invitation != null && !invitation.isAccepted()) {
            List<Long> foreignAccountIdList = getChildAccounts(krwAccountId);

            MemberJoinApiRequestDto memberJoinApiRequestDto = new MemberJoinApiRequestDto(
                    userId, krwAccountId, foreignAccountIdList
            );

            invitation.changeIsAccepted(true);
            return accountClient.joinMember(memberJoinApiRequestDto).getMessage();
        }

        return null;
    }

    public String changeRole(MemberRoleChangeRequestDto memberRoleChangeRequestDto) {
        Long userId = memberRoleChangeRequestDto.getUserId();
        Long krwAccountId = memberRoleChangeRequestDto.getAccountId();
        List<Long> foreignAccountIdList = getChildAccounts(krwAccountId);
        String userRole = memberRoleChangeRequestDto.getRole();

        MemberRoleUpdateApiRequestDto memberRoleUpdateApiRequestDto =
                new MemberRoleUpdateApiRequestDto(userId, krwAccountId, foreignAccountIdList, userRole);

        return accountClient.updateUserRole(memberRoleUpdateApiRequestDto).getMessage();
    }

    public List<Long> getChildAccounts(Long parentAccountId) {
        KrwTravelAccount krwTravelAccount = krwTravelAccountRepository.findByAccountId(parentAccountId);

        List<ForeignTravelAccount> foreignTravelAccounts = krwTravelAccount.getChildAccounts();
        List<Long> foreignAccountIdList = new ArrayList<>();

        for (ForeignTravelAccount account : foreignTravelAccounts) {
            foreignAccountIdList.add(account.getAccountId());
        }

        return foreignAccountIdList;
    }

    public Boolean isInviteMember(Long accountId) {
        Long userId = UserHeaderInterceptor.userId.get();
        String email = userClient.getUserEmail(userId).getUserEmail();

        KrwTravelAccount account = krwTravelAccountRepository.findByAccountId(accountId);

        Invitation invitation = invitationRepository.findByEmailAndAccount(email, account);

        if(invitation != null) {
            return true;
        }

        return false;
    }
}
