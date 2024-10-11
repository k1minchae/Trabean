package com.trabean.internal.service;

import com.trabean.account.domain.Account;
import com.trabean.account.domain.Account.AccountType;
import com.trabean.account.domain.UserAccountRelation;
import com.trabean.account.domain.UserAccountRelation.UserRole;
import com.trabean.account.repository.AccountRepository;
import com.trabean.account.repository.UserAccountRelationRepository;
import com.trabean.common.InternalServerSuccessResponseDTO;
import com.trabean.exception.custom.InvalidPasswordException;
import com.trabean.exception.custom.UserAccountRelationNotFoundException;
import com.trabean.external.msa.user.client.UserClient;
import com.trabean.external.msa.user.dto.request.UserKeyRequestDTO;
import com.trabean.external.msa.user.dto.response.UserKeyResponseDTO;
import com.trabean.external.msa.user.dto.response.UserNameResponseDTO;
import com.trabean.interceptor.UserHeaderInterceptor;
import com.trabean.internal.dto.request.*;
import com.trabean.internal.dto.response.AccountNoResponseDTO;
import com.trabean.internal.dto.response.AdminUserKeyResponseDTO;
import com.trabean.internal.dto.response.TravelAccountMembersResponseDTO;
import com.trabean.internal.dto.response.TravelAccountMembersResponseDTO.Member;
import com.trabean.internal.dto.response.UserRoleResponseDTO;
import com.trabean.util.ValidateInputDTO;
import com.trabean.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.trabean.constant.Constant.PEPPER;

@Service
@Transactional
@RequiredArgsConstructor
public class InternalService {

    private final AccountRepository accountRepository;
    private final UserAccountRelationRepository userAccountRelationRepository;

    private final UserClient userClient;

    private final PasswordEncoder passwordEncoder;

    // 통장 계좌번호 조회 서비스 로직
    public AccountNoResponseDTO getAccountNo(AccountNoRequestDTO requestDTO) {

        String accountNo = ValidationUtil.validateAccount(accountRepository.findById(requestDTO.getAccountId()))
                .getAccountNo();

        return AccountNoResponseDTO.builder()
                .message("통장 계좌번호 조회 성공")
                .accountNo(accountNo)
                .build();
    }

    // 통장 권한 조회 서비스 로직
    public UserRoleResponseDTO getUserRole(UserRoleRequestDTO requestDTO) {

        UserRole userRole = ValidationUtil.validateUserAccountRelation(userAccountRelationRepository.findByUserIdAndAccountId(requestDTO.getUserId(), requestDTO.getAccountId()))
                .getUserRole();

        return UserRoleResponseDTO.builder()
                .message("통장 권한 조회 성공")
                .userRole(userRole)
                .build();
    }

    // 여행통장 결제 권한 변경 서비스 로직
    public InternalServerSuccessResponseDTO updateUserRole(UpdateUserRoleRequestDTO requestDTO) {

        userAccountRelationRepository.updateUserRoleByUserIdAndAccountId(requestDTO.getUserId(), requestDTO.getDomesticAccountId(), requestDTO.getUserRole());

        for (Long foreignAccountId : requestDTO.getForeignAccountIdList()) {
            userAccountRelationRepository.updateUserRoleByUserIdAndAccountId(requestDTO.getUserId(), foreignAccountId, requestDTO.getUserRole());
        }

        return InternalServerSuccessResponseDTO.builder()
                .message("여행통장 결제 권한 변경 성공")
                .build();
    }

    // 결제 비밀번호 검증 서비스 로직
    public InternalServerSuccessResponseDTO verifyPassword(VerifyPasswordRequestDTO requestDTO) {

        String savedPassword = ValidationUtil.validateInput(ValidateInputDTO.builder()
                        .account(accountRepository.findById(requestDTO.getAccountId()))
                        .userAccountRelation(userAccountRelationRepository.findByUserIdAndAccountId(requestDTO.getUserId(), requestDTO.getAccountId()))
                        .isPayable(true)
                        .build())
                .getPassword();

        if (!passwordEncoder.matches(requestDTO.getPassword() + PEPPER, savedPassword)) {
            throw InvalidPasswordException.getInstance();
        }

        return InternalServerSuccessResponseDTO.builder()
                .message("결제 비밀번호 검증 성공")
                .build();
    }

    // 여행통장 가입 서비스 로직
    public InternalServerSuccessResponseDTO joinTravelAccount(JoinTravelAccountRequestDTO requestDTO) {

        // 한화 여행통장에 가입
        UserAccountRelation domesticUserAccountRelation = UserAccountRelation.builder()
                .userId(requestDTO.getUserId())
                .account(Account.builder()
                        .accountId(requestDTO.getDomesticAccountId())
                        .build())
                .userRole(UserRole.NONE_PAYER)
                .build();
        userAccountRelationRepository.save(domesticUserAccountRelation);

        // 한화 여행통장에 연결된 외화 여행통장에 가입
        for (Long foreignAccountId : requestDTO.getForeignAccountIdList()) {
            UserAccountRelation foreignUserAccountRelation = UserAccountRelation.builder()
                    .userId(requestDTO.getUserId())
                    .account(Account.builder()
                            .accountId(foreignAccountId)
                            .build())
                    .userRole(UserRole.NONE_PAYER)
                    .build();
            userAccountRelationRepository.save(foreignUserAccountRelation);
        }

        return InternalServerSuccessResponseDTO.builder()
                .message("여행통장 가입 성공")
                .build();
    }

    // 통장 주인의 userKey 조회 서비스 로직
    public AdminUserKeyResponseDTO getAdminUserKey(AdminUserKeyRequestDTO requestDTO) {

        List<UserAccountRelation> userAccountRelationList = ValidationUtil.validateUserAccountRelationList(userAccountRelationRepository.findAllByAccountId(requestDTO.getAccountId()));

        Long userId = userAccountRelationList.stream()
                .filter(relation -> relation.getUserRole() == UserRole.ADMIN)
                .map(UserAccountRelation::getUserId)
                .findFirst()
                .orElseThrow(UserAccountRelationNotFoundException::getInstance);

        // User 서버에 userKey 조회 요청
        UserKeyRequestDTO userKeyRequestDTO = UserKeyRequestDTO.builder()
                .userId(userId)
                .build();
        UserKeyResponseDTO userKeyResponseDTO = userClient.getUserKey(userKeyRequestDTO);

        return AdminUserKeyResponseDTO.builder()
                .userKey(userKeyResponseDTO.getUserKey())
                .build();
    }

    // 여행통장 비밀번호 변경 서비스 로직
    public InternalServerSuccessResponseDTO updateTravelAccountPassword(UpdateTravelAccountPasswordRequestDTO requestDTO) {

        String hashedPassword = passwordEncoder.encode(requestDTO.getPassword() + PEPPER);

        accountRepository.updatePasswordById(requestDTO.getDomesticAccountId(), hashedPassword);

        for(Long foreignAccountId : requestDTO.getForeignAccountIdList()) {
            accountRepository.updatePasswordById(foreignAccountId, hashedPassword);
        }

        return InternalServerSuccessResponseDTO.builder()
                .message("비밀번호 변경 성공")
                .build();
    }

    // 한화 여행통장 멤버 목록 조회 API
    public TravelAccountMembersResponseDTO getTravelAccountMembers(TravelAccountMembersRequestDTO requestDTO) {

        ValidationUtil.validateInput(ValidateInputDTO.builder()
                .account(accountRepository.findById(requestDTO.getAccountId()))
                .userAccountRelation(userAccountRelationRepository.findByUserIdAndAccountId(requestDTO.getUserId(), requestDTO.getAccountId()))
                .accountType(AccountType.DOMESTIC)
                .build());

        List<UserAccountRelation> userAccountRelations = ValidationUtil.validateUserAccountRelationList(userAccountRelationRepository.findAllByAccountId(requestDTO.getAccountId()));

        List<Member> members = new ArrayList<>();

        // User 서버에 userId로 userName 조회하는 요청을 모든 멤버에 대해 보냄
        for (UserAccountRelation member : userAccountRelations) {
            UserNameResponseDTO userNameResponseDTO = userClient.getUserName(member.getUserId());

            members.add(Member.builder()
                    .userId(member.getUserId())
                    .userName(userNameResponseDTO.getUserName())
                    .role(member.getUserRole())
                    .build());
        }

        return TravelAccountMembersResponseDTO.builder()
                .userId(requestDTO.getUserId())
                .memberCount((long) members.size())
                .members(members).build();
    }
}
