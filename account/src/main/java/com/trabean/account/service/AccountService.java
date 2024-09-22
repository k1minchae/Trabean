package com.trabean.account.service;

import com.trabean.account.domain.Account;
import com.trabean.account.domain.UserAccountRelation;
import com.trabean.account.dto.request.*;
import com.trabean.account.dto.response.*;
import com.trabean.account.repository.AccountRepository;
import com.trabean.account.repository.UserAccountRelationRepository;
import com.trabean.exception.AccountNotFoundException;
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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.trabean.constant.Constants.DOMESTIC_TRAVEL_ACCOUNT_TYPE_UNIQUE_NO;
import static com.trabean.constant.Constants.PERSONAL_ACCOUNT_TYPE_UNIQUE_NO;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserAccountRelationRepository userAccountRelationRepository;

    private final DomesticClient domesticClient;

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
    public AccountDetailResponseDTO getAccountDetail(AccountDetailRequestDTO requestDTO, String startDate, String endDate, String transactionType) {
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

        ResponseCode responseCode;
        String responseMessage;
        String bankName;
        Long accountBalance;
        List<AccountDetailResponseDTO.Transaction> transactionList;

        try {
            InquireDemandDepositAccountResponseDTO inquireDemandDepositAccountResponseDTO = domesticClient.inquireDemandDepositAccount(inquireDemandDepositAccountRequestDTO);

            bankName = inquireDemandDepositAccountResponseDTO.getRec().getBankName();
            accountBalance = inquireDemandDepositAccountResponseDTO.getRec().getAccountBalance();

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

            responseCode = inquireTransactionHistoryListResponseDTO.getHeader().getResponseCode();
            responseMessage = inquireTransactionHistoryListResponseDTO.getHeader().getResponseMessage();
            transactionList = getTransactionList(inquireTransactionHistoryListResponseDTO);

        } catch (CustomFeignClientException e) {
            responseCode = e.getErrorResponse().getResponseCode();
            responseMessage = e.getErrorResponse().getResponseMessage();

            return AccountDetailResponseDTO.builder()
                    .responseCode(responseCode)
                    .responseMessage(responseMessage)
                    .build();
        }

        return AccountDetailResponseDTO.builder()
                .responseCode(responseCode)
                .responseMessage(responseMessage)
                .bankName(bankName)
                .accountBalance(accountBalance)
                .transactionList(transactionList)
                .build();
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

    // SSAFY API 통장 목록 조회 responseDTO -> 통장 목록 리스트
    private List<AccountListResponseDTO.Account> getAccountList(InquireDemandDepositAccountListResponseDTO inquireDemandDepositAccountListResponseDTO) {

        return inquireDemandDepositAccountListResponseDTO.getRec().stream()
                .map(rec -> {
                    Long accountId = accountRepository.findByAccountNo(rec.getAccountNo())
                            .orElseThrow(() -> new AccountNotFoundException("해당 계좌를 찾을 수 없습니다."))
                            .getAccountId();

                    return AccountListResponseDTO.Account.builder()
                            .accountId(accountId)
                            .bankCode(rec.getBankCode())
                            .bankName(rec.getBankName())
                            .accountNo(rec.getAccountNo())
                            .accountBalance(rec.getAccountBalance())
                            .build();
                })
                .collect(Collectors.toList());
    }

    // SSAFY API 계좌 거래 내역 조회 responseDTO -> 거래 내역 리스트
    private List<AccountDetailResponseDTO.Transaction> getTransactionList(InquireTransactionHistoryListResponseDTO inquireTransactionHistoryListResponseDTO) {
        return inquireTransactionHistoryListResponseDTO.getRec().getList().stream()
                .map(item -> AccountDetailResponseDTO.Transaction.builder()
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
