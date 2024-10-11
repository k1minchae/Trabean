package com.trabean.account.service;

import com.trabean.account.domain.Account;
import com.trabean.account.domain.UserAccountRelation;
import com.trabean.account.dto.request.*;
import com.trabean.account.dto.response.*;
import com.trabean.account.repository.AccountRepository;
import com.trabean.account.repository.UserAccountRelationRepository;
import com.trabean.exception.AccountNotFoundException;
import com.trabean.exception.InvalidPasswordException;
import com.trabean.exception.InvalidRequestException;
import com.trabean.exception.UserAccountRelationNotFoundException;
import com.trabean.ssafy.api.account.domestic.client.DomesticClient;
import com.trabean.ssafy.api.account.domestic.dto.requestDTO.CreateDemandDepositAccountRequestDTO;
import com.trabean.ssafy.api.account.domestic.dto.requestDTO.InquireDemandDepositAccountListRequestDTO;
import com.trabean.ssafy.api.account.domestic.dto.requestDTO.InquireDemandDepositAccountRequestDTO;
import com.trabean.ssafy.api.account.domestic.dto.requestDTO.InquireTransactionHistoryListRequestDTO;
import com.trabean.ssafy.api.account.domestic.dto.responseDTO.CreateDemandDepositAccountResponseDTO;
import com.trabean.ssafy.api.account.domestic.dto.responseDTO.InquireDemandDepositAccountListResponseDTO;
import com.trabean.ssafy.api.account.domestic.dto.responseDTO.InquireDemandDepositAccountResponseDTO;
import com.trabean.ssafy.api.account.domestic.dto.responseDTO.InquireTransactionHistoryListResponseDTO;
import com.trabean.ssafy.api.config.CustomFeignClientException;
import com.trabean.ssafy.api.response.code.ResponseCode;
import com.trabean.util.RequestHeader;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.trabean.constant.Constants.*;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserAccountRelationRepository userAccountRelationRepository;

    private final DomesticClient domesticClient;

    private final PasswordEncoder passwordEncoder;

    // 개인 통장 생성 서비스 로직
    public CreatePersonalAccountResponseDTO createPersonalAccount(CreatePersonalAccountRequestDTO requestDTO) {
        String userKey = requestDTO.getUserKey();
        Long userId = requestDTO.getUserId();
        String password = requestDTO.getPassword();

        // SSAFY API 계좌 생성 요청
        CreateDemandDepositAccountRequestDTO createDemandDepositAccountRequestDTO = CreateDemandDepositAccountRequestDTO.builder()
                .header(RequestHeader.builder()
                        .apiName("createDemandDepositAccount")
                        .userKey(userKey)
                        .build())
                .accountTypeUniqueNo(PERSONAL_ACCOUNT_TYPE_UNIQUE_NO)
                .build();

        ResponseCode responseCode;
        String responseMessage;

        try {
            CreateDemandDepositAccountResponseDTO createDemandDepositAccountResponseDTO = domesticClient.createDemandDepositAccount(createDemandDepositAccountRequestDTO);

            responseCode = createDemandDepositAccountResponseDTO.getHeader().getResponseCode();
            responseMessage = createDemandDepositAccountResponseDTO.getHeader().getResponseMessage();

            String accountNo = createDemandDepositAccountResponseDTO.getRec().getAccountNo();
            String hashedPassword = passwordEncoder.encode(password + PEPPER);

            // Account 테이블에 저장
            Account account = Account.builder()
                    .accountNo(accountNo)
                    .password(hashedPassword)
                    .userId(userId)
                    .build();

            Account savedAccount = accountRepository.save(account);

            // UserAccountRelation 테이블에 저장
            UserAccountRelation userAccountRelation = UserAccountRelation.builder()
                    .userId(userId)
                    .account(savedAccount)
                    .userRole(UserAccountRelation.UserRole.ADMIN)
                    .build();

            userAccountRelationRepository.save(userAccountRelation);

        } catch (CustomFeignClientException e) {
            responseCode = e.getErrorResponse().getResponseCode();
            responseMessage = e.getErrorResponse().getResponseMessage();
        }
        return CreatePersonalAccountResponseDTO.builder()
                .responseCode(responseCode)
                .responseMessage(responseMessage)
                .build();
    }

    // 한화 여행통장 생성 서비스 로직
    public CreateDomesticTravelAccountResponseDTO createDomesticTravelAccount(CreateDomesticTravelAccountRequestDTO requestDTO) {
        String userKey = requestDTO.getUserKey();
        Long userId = requestDTO.getUserId();
        String password = requestDTO.getPassword();

        // SSAFY API 계좌 생성 요청
        CreateDemandDepositAccountRequestDTO createDemandDepositAccountRequestDTO = CreateDemandDepositAccountRequestDTO.builder()
                .header(RequestHeader.builder()
                        .apiName("createDemandDepositAccount")
                        .userKey(userKey)
                        .build())
                .accountTypeUniqueNo(DOMESTIC_TRAVEL_ACCOUNT_TYPE_UNIQUE_NO)
                .build();

        ResponseCode responseCode;
        String responseMessage;

        try {
            CreateDemandDepositAccountResponseDTO createDemandDepositAccountResponseDTO = domesticClient.createDemandDepositAccount(createDemandDepositAccountRequestDTO);

            responseCode = createDemandDepositAccountResponseDTO.getHeader().getResponseCode();
            responseMessage = createDemandDepositAccountResponseDTO.getHeader().getResponseMessage();

            String accountNo = createDemandDepositAccountResponseDTO.getRec().getAccountNo();

            // Account 테이블에 저장
            Account account = Account.builder()
                    .accountNo(accountNo)
                    .password(password)
                    .userId(userId)
                    .build();

            Account savedAccount = accountRepository.save(account);

            // UserAccountRelation 테이블에 저장
            UserAccountRelation userAccountRelation = UserAccountRelation.builder()
                    .userId(userId)
                    .account(savedAccount)
                    .userRole(UserAccountRelation.UserRole.ADMIN)
                    .build();

            userAccountRelationRepository.save(userAccountRelation);

        } catch (CustomFeignClientException e) {
            responseCode = e.getErrorResponse().getResponseCode();
            responseMessage = e.getErrorResponse().getResponseMessage();
        }
        return CreateDomesticTravelAccountResponseDTO.builder()
                .responseCode(responseCode)
                .responseMessage(responseMessage)
                .build();
    }

    // 통장 목록 조회 서비스 로직
    public AccountListResponseDTO getAccountList(AccountListRequestDTO requestDTO) {
        String userKey = requestDTO.getUserKey();

        // SSAFY API 계좌 목록 조회 요청
        InquireDemandDepositAccountListRequestDTO inquireDemandDepositAccountListRequestDTO = InquireDemandDepositAccountListRequestDTO.builder()
                .header(RequestHeader.builder()
                        .apiName("inquireDemandDepositAccountList")
                        .userKey(userKey)
                        .build())
                .build();

        ResponseCode responseCode;
        String responseMessage;
        List<AccountListResponseDTO.Account> accountList;

        try {
            InquireDemandDepositAccountListResponseDTO inquireDemandDepositAccountListResponseDTO = domesticClient.inquireDemandDepositAccountList(inquireDemandDepositAccountListRequestDTO);

            responseCode = inquireDemandDepositAccountListResponseDTO.getHeader().getResponseCode();
            responseMessage = inquireDemandDepositAccountListResponseDTO.getHeader().getResponseMessage();
            accountList = getAccountList(inquireDemandDepositAccountListResponseDTO);

        } catch (CustomFeignClientException e) {
            responseCode = e.getErrorResponse().getResponseCode();
            responseMessage = e.getErrorResponse().getResponseMessage();
            accountList = new ArrayList<>();
        }
        return AccountListResponseDTO.builder()
                .responseCode(responseCode)
                .responseMessage(responseMessage)
                .accountList(accountList)
                .build();
    }

    // 개인 통장 상세 조회 서비스 로직
    public PersonalAccountDetailResponseDTO getAccountDetail(PersonalAccountDetailRequestDTO requestDTO, String startDate, String endDate, String transactionType) {
        String userKey = requestDTO.getUserKey();
        String accountNo = requestDTO.getAccountNo();

        // SSAFY 계좌 조회 (단건) 요청
        InquireDemandDepositAccountRequestDTO inquireDemandDepositAccountRequestDTO = InquireDemandDepositAccountRequestDTO.builder()
                .header(RequestHeader.builder()
                        .apiName("inquireDemandDepositAccount")
                        .userKey(userKey)
                        .build())
                .accountNo(accountNo)
                .build();

        try {
            InquireDemandDepositAccountResponseDTO inquireDemandDepositAccountResponseDTO = domesticClient.inquireDemandDepositAccount(inquireDemandDepositAccountRequestDTO);

            String bankName = inquireDemandDepositAccountResponseDTO.getRec().getBankName();
            Long accountBalance = inquireDemandDepositAccountResponseDTO.getRec().getAccountBalance();

            // SSAFY 계좌 거래 내역 조회 요청
            InquireTransactionHistoryListRequestDTO inquireTransactionHistoryListRequestDTO = InquireTransactionHistoryListRequestDTO.builder()
                    .header(RequestHeader.builder()
                            .apiName("inquireTransactionHistoryList")
                            .userKey(userKey)
                            .build())
                    .accountNo(accountNo)
                    .startDate(startDate)
                    .endDate(endDate)
                    .transactionType(transactionType)
                    .orderByType("DESC")
                    .build();

            InquireTransactionHistoryListResponseDTO inquireTransactionHistoryListResponseDTO = domesticClient.inquireTransactionHistoryList(inquireTransactionHistoryListRequestDTO);

            ResponseCode responseCode = inquireTransactionHistoryListResponseDTO.getHeader().getResponseCode();
            String responseMessage = inquireTransactionHistoryListResponseDTO.getHeader().getResponseMessage();
            List<PersonalAccountDetailResponseDTO.Transaction> transactionList = getTransactionList(inquireTransactionHistoryListResponseDTO);

            return PersonalAccountDetailResponseDTO.builder()
                    .responseCode(responseCode)
                    .responseMessage(responseMessage)
                    .bankName(bankName)
                    .accountBalance(accountBalance)
                    .transactionList(transactionList)
                    .build();
        } catch (CustomFeignClientException e) {
            ResponseCode responseCode = e.getErrorResponse().getResponseCode();
            String responseMessage = e.getErrorResponse().getResponseMessage();

            return PersonalAccountDetailResponseDTO.builder()
                    .responseCode(responseCode)
                    .responseMessage(responseMessage)
                    .build();
        }
    }

    // 한화 여행통장 상세 조회 서비스 로직
    public DomesticTravelAccountDetailResponseDTO getDomesticTravelAccountDetail(DomesticTravelAccountDetailRequestDTO requestDTO, String startDate, String endDate, String transactionType) {
        String userKey = requestDTO.getUserKey();
        String accountNo = requestDTO.getAccountNo();

        // SSAFY 계좌 조회 (단건) 요청
        InquireDemandDepositAccountRequestDTO inquireDemandDepositAccountRequestDTO = InquireDemandDepositAccountRequestDTO.builder()
                .header(RequestHeader.builder()
                        .apiName("inquireDemandDepositAccount")
                        .userKey(userKey)
                        .build())
                .accountNo(accountNo)
                .build();

        try {
            InquireDemandDepositAccountResponseDTO inquireDemandDepositAccountResponseDTO = domesticClient.inquireDemandDepositAccount(inquireDemandDepositAccountRequestDTO);

            String bankName = inquireDemandDepositAccountResponseDTO.getRec().getBankName();
            Long accountBalance = inquireDemandDepositAccountResponseDTO.getRec().getAccountBalance();

            // SSAFY 계좌 거래 내역 조회 요청
            InquireTransactionHistoryListRequestDTO inquireTransactionHistoryListRequestDTO = InquireTransactionHistoryListRequestDTO.builder()
                    .header(RequestHeader.builder()
                            .apiName("inquireTransactionHistoryList")
                            .userKey(userKey)
                            .build())
                    .accountNo(accountNo)
                    .startDate(startDate)
                    .endDate(endDate)
                    .transactionType(transactionType)
                    .orderByType("DESC")
                    .build();

            InquireTransactionHistoryListResponseDTO inquireTransactionHistoryListResponseDTO = domesticClient.inquireTransactionHistoryList(inquireTransactionHistoryListRequestDTO);

            ResponseCode responseCode = inquireTransactionHistoryListResponseDTO.getHeader().getResponseCode();
            String responseMessage = inquireTransactionHistoryListResponseDTO.getHeader().getResponseMessage();
            List<DomesticTravelAccountDetailResponseDTO.Transaction> transactionList = getDomesticTravelAccountTransactionList(inquireTransactionHistoryListResponseDTO);

            return DomesticTravelAccountDetailResponseDTO.builder()
                    .responseCode(responseCode)
                    .responseMessage(responseMessage)
                    .bankName(bankName)
                    .accountBalance(accountBalance)
                    .transactionList(transactionList)
                    .build();
        } catch (CustomFeignClientException e) {
            ResponseCode responseCode = e.getErrorResponse().getResponseCode();
            String responseMessage = e.getErrorResponse().getResponseMessage();

            return DomesticTravelAccountDetailResponseDTO.builder()
                    .responseCode(responseCode)
                    .responseMessage(responseMessage)
                    .build();
        }
    }

    // 통장 계좌번호 조회 서비스 로직
    public AccountNoResponseDTO getAccountNoById(AccountNoRequestDTO requestDTO) {
        Long accountId = requestDTO.getAccountId();

        String accountNo = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("해당 계좌를 찾을 수 없습니다."))
                .getAccountNo();

        return AccountNoResponseDTO.builder()
                .message("통장 계좌번호 조회 성공")
                .accountNo(accountNo)
                .build();
    }

    // 통장 권한 조회 서비스 로직
    public UserRoleResponseDTO getUserRoleByUserIdAndAccountId(UserRoleRequestDTO requestDTO) {
        Long userId = requestDTO.getUserId();
        Long accountId = requestDTO.getAccountId();

        UserAccountRelation.UserRole userRole = userAccountRelationRepository.findByUserIdAndAccountId(userId, accountId)
                .orElseThrow(() -> new UserAccountRelationNotFoundException("잘못된 요청입니다."))
                .getUserRole();

        return UserRoleResponseDTO.builder()
                .message("통장 권한 조회 성공")
                .userRole(userRole)
                .build();
    }

    // 통장 권한 변경 서비스 로직
    public UpdateUserRoleResponseDTO updateUserRole(UpdateUserRoleRequestDTO requestDTO) {
        Long userId = requestDTO.getUserId();
        Long accountId = requestDTO.getAccountId();
        UserAccountRelation.UserRole userRole = requestDTO.getUserRole();

        int updatedTuples = userAccountRelationRepository.updateUserRoleByUserIdAndAccountId(userId, accountId, userRole);

        if(updatedTuples == 1){
            return UpdateUserRoleResponseDTO.builder()
                    .message("통장 권한 변경 성공")
                    .build();
        }
        else{
            throw new UserAccountRelationNotFoundException("잘못된 요청입니다.");
        }
    }

    // 통장 비밀번호 검증 서비스 로직
    public VerifyPasswordResponseDTO verifyPassword(VerifyPasswordRequestDTO requestDTO) {
        if(requestDTO.getAccountId() == null && requestDTO.getAccountNo() == null){
            throw new InvalidRequestException("잘못된 요청입니다.");
        }

        Account account;

        if(requestDTO.getAccountId() != null){
            Long accountId = requestDTO.getAccountId();
            account = accountRepository.findById(accountId).orElseThrow(() -> new AccountNotFoundException("해당 계좌를 찾을 수 없습니다."));
        }
        else{
            String accountNo = requestDTO.getAccountNo();
            account = accountRepository.findByAccountNo(accountNo).orElseThrow(() -> new AccountNotFoundException("해당 계좌를 찾을 수 없습니다."));
        }

        String password = requestDTO.getPassword();
        String savedPassword = account.getPassword();

        if(passwordEncoder.matches(password + PEPPER, savedPassword)){
           return VerifyPasswordResponseDTO.builder()
                   .message("통장 비밀번호 검증 성공")
                   .build();
        }
        else{
            throw new InvalidPasswordException("비밀번호가 틀렸습니다.");
        }
    }

    // SSAFY API 통장 목록 조회 responseDTO -> 통장 목록 리스트
    private List<AccountListResponseDTO.Account> getAccountList(InquireDemandDepositAccountListResponseDTO inquireDemandDepositAccountListResponseDTO) {
        return inquireDemandDepositAccountListResponseDTO.getRec().stream()
                .map(rec -> AccountListResponseDTO.Account.builder()
                        .bankName(rec.getBankName())
                        .accountNo(rec.getAccountNo())
                        .accountBalance(rec.getAccountBalance())
                        .build())
                .collect(Collectors.toList());
    }

    // SSAFY API 계좌 거래 내역 조회 responseDTO -> 거래 내역 리스트
    private List<PersonalAccountDetailResponseDTO.Transaction> getTransactionList(InquireTransactionHistoryListResponseDTO inquireTransactionHistoryListResponseDTO) {
        return inquireTransactionHistoryListResponseDTO.getRec().getList().stream()
                .map(item -> PersonalAccountDetailResponseDTO.Transaction.builder()
                        .transactionType(item.getTransactionType())
                        .transactionSummary(item.getTransactionSummary())
                        .transactionDate(item.getTransactionDate())
                        .transactionTime(item.getTransactionTime())
                        .transactionBalance(item.getTransactionBalance())
                        .transactionAfterBalance(item.getTransactionAfterBalance())
                        .build())
                .collect(Collectors.toList());
    }

    // SSAFY API 계좌 거래 내역 조회 responseDTO -> 거래 내역 리스트
    private List<DomesticTravelAccountDetailResponseDTO.Transaction> getDomesticTravelAccountTransactionList(InquireTransactionHistoryListResponseDTO inquireTransactionHistoryListResponseDTO) {
        return inquireTransactionHistoryListResponseDTO.getRec().getList().stream()
                .map(item -> DomesticTravelAccountDetailResponseDTO.Transaction.builder()
                        .transactionType(item.getTransactionType())
                        .transactionSummary(item.getTransactionSummary())
                        .transactionDate(item.getTransactionDate())
                        .transactionTime(item.getTransactionTime())
                        .transactionBalance(item.getTransactionBalance())
                        .transactionAfterBalance(item.getTransactionAfterBalance())
                        .build())
                .collect(Collectors.toList());
    }

}
