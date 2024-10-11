package com.trabean.account.service;

import com.trabean.account.domain.Account;
import com.trabean.account.domain.Account.AccountType;
import com.trabean.account.domain.UserAccountRelation;
import com.trabean.account.domain.UserAccountRelation.UserRole;
import com.trabean.account.dto.request.*;
import com.trabean.account.dto.request.UpdateAccountTransferLimitRequestDTO;
import com.trabean.account.dto.response.*;
import com.trabean.account.dto.response.DomesticTravelAccountMemberListResponseDTO.Member;
import com.trabean.account.repository.AccountRepository;
import com.trabean.account.repository.UserAccountRelationRepository;
import com.trabean.common.InternalServerSuccessResponseDTO;
import com.trabean.common.SsafySuccessResponseDTO;
import com.trabean.exception.custom.InvalidPasswordException;
import com.trabean.exception.custom.UserAccountRelationNotFoundException;
import com.trabean.external.msa.travel.client.TravelClient;
import com.trabean.external.msa.travel.dto.request.SaveDomesticTravelAccountRequestDTO;
import com.trabean.external.msa.travel.dto.request.SaveForeignTravelAccountRequestDTO;
import com.trabean.external.msa.travel.dto.response.DomesticTravelAccountInfoResponseDTO;
import com.trabean.external.msa.travel.dto.response.ParentAccountIdResponseDTO;
import com.trabean.external.msa.user.client.UserClient;
import com.trabean.external.msa.user.dto.request.MainAccountIdRequestDTO;
import com.trabean.external.msa.user.dto.request.UserKeyRequestDTO;
import com.trabean.external.msa.user.dto.response.UserKeyResponseDTO;
import com.trabean.external.msa.user.dto.response.UserNameResponseDTO;
import com.trabean.external.ssafy.domestic.client.DomesticClient;
import com.trabean.external.ssafy.domestic.dto.request.*;
import com.trabean.external.ssafy.domestic.dto.response.*;
import com.trabean.external.ssafy.foriegn.client.ForeignClient;
import com.trabean.external.ssafy.foriegn.dto.request.CreateForeignCurrencyDemandDepositAccountRequestDTO;
import com.trabean.external.ssafy.foriegn.dto.request.InquireForeignCurrencyDemandDepositAccountRequestDTO;
import com.trabean.external.ssafy.foriegn.dto.response.CreateForeignCurrencyDemandDepositAccountResponseDTO;
import com.trabean.external.ssafy.foriegn.dto.response.InquireForeignCurrencyDemandDepositAccountResponseDTO;
import com.trabean.external.ssafy.memo.client.MemoClient;
import com.trabean.external.ssafy.memo.dto.request.TransactionMemoRequestDTO;
import com.trabean.interceptor.UserHeaderInterceptor;
import com.trabean.util.RequestHeader;
import com.trabean.util.ValidateInputDTO;
import com.trabean.util.ValidationUtil;
import jakarta.ws.rs.InternalServerErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.trabean.constant.Constant.*;

@Service
//@Transactional
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserAccountRelationRepository userAccountRelationRepository;

    private final DomesticClient domesticClient;
    private final ForeignClient foreignClient;
    private final MemoClient memoClient;

    private final UserClient userClient;
    private final TravelClient travelClient;

    private final PasswordEncoder passwordEncoder;

//    // 통장 목록 조회 서비스 로직
//    public AccountListResponseDTO getAccountList() {
//
//        // SSAFY 금융 API 계좌 목록 조회 요청
//        InquireDemandDepositAccountListRequestDTO inquireDemandDepositAccountListRequestDTO = InquireDemandDepositAccountListRequestDTO.builder()
//                .header(RequestHeader.builder()
//                        .apiName("inquireDemandDepositAccountList")
//                        .userKey(UserHeaderInterceptor.userKey.get())
//                        .build())
//                .build();
//        InquireDemandDepositAccountListResponseDTO inquireDemandDepositAccountListResponseDTO = domesticClient.inquireDemandDepositAccountList(inquireDemandDepositAccountListRequestDTO);
//
//        if (inquireDemandDepositAccountListResponseDTO.getRec() == null) {
//            return AccountListResponseDTO.builder()
//                    .mainAccount(null)
//                    .accountList(new ArrayList<>())
//                    .build();
//        }
//        return getAccountList(inquireDemandDepositAccountListResponseDTO);
//    }
//
//    // SSAFY 금융 API 게좌 목록 responseDTO -> 통장 목록 조회 responseDTO
//    private AccountListResponseDTO getAccountList(InquireDemandDepositAccountListResponseDTO inquireDemandDepositAccountListResponseDTO) {
//        AccountListResponseDTO.Account mainAccount = null;
//        List<AccountListResponseDTO.Account> accountList = new ArrayList<>();
//
//        // User 서버에 userId로 mainAccountId 반환 요청
//        Long mainAccountId = userClient.getMainAccountId(UserHeaderInterceptor.userId.get()).getMainAccountId();
//
//        // Travel 서버에 한화 여행통장이름 이름 반환 요청
//        for (InquireDemandDepositAccountListResponseDTO.REC account : inquireDemandDepositAccountListResponseDTO.getRec()) {
//            Account savedAccount = ValidationUtil.validateAccount(accountRepository.findByAccountNo(account.getAccountNo()));
//            String savedAccountName = savedAccount.getAccountType() == AccountType.DOMESTIC
//                    ? travelClient.getDomesticTravelAccountInfo(savedAccount.getAccountId()).getAccountName()
//                    : "개인 입출금 통장";
//
//            AccountListResponseDTO.Account dtoAccount = AccountListResponseDTO.Account.builder()
//                    .accountId(savedAccount.getAccountId())
//                    .accountNo(account.getAccountNo())
//                    .accountName(savedAccountName)
//                    .bankName(account.getBankName())
//                    .accountBalance(account.getAccountBalance())
//                    .accountType(savedAccount.getAccountType())
//                    .build();
//
//            if (savedAccount.getAccountId().equals(mainAccountId)) {
//                mainAccount = dtoAccount;
//            } else {
//                accountList.add(dtoAccount);
//            }
//        }
//
//        return AccountListResponseDTO.builder()
//                .mainAccount(mainAccount)
//                .accountList(accountList)
//                .build();
//    }

    // 통장 목록 조회 서비스 로직
    public AccountListResponseDTO getAccountList() {

//        System.out.println(UserHeaderInterceptor.userId.get());
//        System.out.println(UserHeaderInterceptor.userKey.get());

//        System.out.println("계좌 목록 조회 시작");

        // SSAFY 금융 API 계좌 목록 조회 요청
        InquireDemandDepositAccountListRequestDTO inquireDemandDepositAccountListRequestDTO = InquireDemandDepositAccountListRequestDTO.builder()
                .header(RequestHeader.builder()
                        .apiName("inquireDemandDepositAccountList")
                        .userKey(UserHeaderInterceptor.userKey.get())
                        .build())
                .build();
        InquireDemandDepositAccountListResponseDTO inquireDemandDepositAccountListResponseDTO = domesticClient.inquireDemandDepositAccountList(inquireDemandDepositAccountListRequestDTO);

//        System.out.println("계좌 목록 조회 끝");

        // 본인이 여행통장 주인이 아닌 통장들에 대한 로직
        List<UserAccountRelation> userAccountRelation = ValidationUtil.validateUserAccountRelationList(userAccountRelationRepository.findAllByUserId(UserHeaderInterceptor.userId.get()));

        // 한화 여행통장(멤버)
        List<UserAccountRelation> adminRelationsForDomesticNonAdminAccounts = userAccountRelation.stream()
                .filter(relation -> relation.getAccount().getAccountType() == Account.AccountType.DOMESTIC &&
                        relation.getUserRole() != UserAccountRelation.UserRole.ADMIN)
                .map(relation -> userAccountRelationRepository.findByAccount_AccountIdAndUserRole(relation.getAccount().getAccountId(), UserAccountRelation.UserRole.ADMIN))
                .flatMap(List::stream)
                .toList();

        for (UserAccountRelation u : adminRelationsForDomesticNonAdminAccounts) {

//            System.out.println("API 요청한 사람 : " + UserHeaderInterceptor.userId.get());
//            System.out.println("여행통장 주인 : " + u.getUserId());
//            System.out.println(u.getUserRole());
            UserKeyRequestDTO userKeyRequestDTO = UserKeyRequestDTO.builder()
                    .userId(u.getUserId())
                    .build();
            UserKeyResponseDTO userKeyResponseDTO = userClient.getUserKey(userKeyRequestDTO);

            String accountNo = ValidationUtil.validateAccount(accountRepository.findById(u.getAccount().getAccountId())).getAccountNo();

            // SSAFY 금융 API 계좌 조회 (단건) 요청
            InquireDemandDepositAccountRequestDTO inquireDemandDepositAccountRequestDTO = InquireDemandDepositAccountRequestDTO.builder()
                    .header(RequestHeader.builder()
                            .apiName("inquireDemandDepositAccount")
                            .userKey(userKeyResponseDTO.getUserKey())
                            .build())
                    .accountNo(accountNo)
                    .build();
            InquireDemandDepositAccountResponseDTO inquireDemandDepositAccountResponseDTO = domesticClient.inquireDemandDepositAccount(inquireDemandDepositAccountRequestDTO);

            inquireDemandDepositAccountListResponseDTO.getRec().add((inquireDemandDepositAccountResponseDTO.getRec()));
        }

        if (inquireDemandDepositAccountListResponseDTO.getRec() == null) {
            return AccountListResponseDTO.builder()
                    .mainAccount(null)
                    .accountList(new ArrayList<>())
                    .build();
        }
        return getAccountList(inquireDemandDepositAccountListResponseDTO);
    }

    // SSAFY 금융 API 게좌 목록 responseDTO -> 통장 목록 조회 responseDTO
    private AccountListResponseDTO getAccountList(InquireDemandDepositAccountListResponseDTO inquireDemandDepositAccountListResponseDTO) {
        AccountListResponseDTO.Account mainAccount = null;
        List<AccountListResponseDTO.Account> accountList = new ArrayList<>();

        // User 서버에 userId로 mainAccountId 반환 요청
        Long mainAccountId = userClient.getMainAccountId(UserHeaderInterceptor.userId.get()).getMainAccountId();

        // Travel 서버에 한화 여행통장이름 이름 반환 요청
        for (InquireDemandDepositAccountListResponseDTO.REC account : inquireDemandDepositAccountListResponseDTO.getRec()) {
            Account savedAccount = ValidationUtil.validateAccount(accountRepository.findByAccountNo(account.getAccountNo()));
            String savedAccountName = savedAccount.getAccountType() == AccountType.DOMESTIC
                    ? travelClient.getDomesticTravelAccountInfo(savedAccount.getAccountId()).getAccountName()
                    : "개인 입출금 통장";

            AccountListResponseDTO.Account dtoAccount = AccountListResponseDTO.Account.builder()
                    .accountId(savedAccount.getAccountId())
                    .accountNo(account.getAccountNo())
                    .accountName(savedAccountName)
                    .bankName(account.getBankName())
                    .accountBalance(account.getAccountBalance())
                    .accountType(savedAccount.getAccountType())
                    .build();

            if (savedAccount.getAccountId().equals(mainAccountId)) {
                mainAccount = dtoAccount;
            } else {
                accountList.add(dtoAccount);
            }
        }

        return AccountListResponseDTO.builder()
                .mainAccount(mainAccount)
                .accountList(accountList)
                .build();
    }

    // 최근 이체 목록 조회 서비스 로직
    public RecentTransactionListResponseDTO getRecentTransactionList(Long accountId, String startDate, String endDate) {

        String accountNo = ValidationUtil.validateInput(ValidateInputDTO.builder()
                    .account(accountRepository.findById(accountId))
                    .userAccountRelation(userAccountRelationRepository.findByUserIdAndAccountId(UserHeaderInterceptor.userId.get(), accountId))
                    .isPayable(true)
                    .build())
                .getAccountNo();

        // SSAFY 금융 API 계좌 거래 내역 조회 요청
        InquireTransactionHistoryListRequestDTO inquireTransactionHistoryListRequestDTO = InquireTransactionHistoryListRequestDTO.builder()
                .header(RequestHeader.builder()
                        .apiName("inquireTransactionHistoryList")
                        .userKey(UserHeaderInterceptor.userKey.get())
                        .build())
                .accountNo(accountNo)
                .startDate(startDate)
                .endDate(endDate)
                .transactionType("D")
                .orderByType("DESC")
                .build();
        InquireTransactionHistoryListResponseDTO inquireTransactionHistoryListResponseDTO = domesticClient.inquireTransactionHistoryList(inquireTransactionHistoryListRequestDTO);

        return getUniqueLastTransactionList(inquireTransactionHistoryListResponseDTO);
    }

    // SSAFY 금융 API 계좌 거래 내역 responseDTO -> 최근 이체 목록 조회 responseDTO
    public RecentTransactionListResponseDTO getUniqueLastTransactionList(InquireTransactionHistoryListResponseDTO inquireTransactionHistoryListResponseDTO) {
        List<RecentTransactionListResponseDTO.Info> accountList = inquireTransactionHistoryListResponseDTO.getRec().getList().stream()
                .filter(transactionHistory -> transactionHistory.getTransactionAccountNo() != null && !transactionHistory.getTransactionAccountNo().trim().isEmpty())
                .map(transactionHistory -> {
                    Account account = ValidationUtil.validateAccount(accountRepository.findByAccountNo(transactionHistory.getTransactionAccountNo()));

                    List<UserAccountRelation> userAccountRelationList = ValidationUtil.validateUserAccountRelationList(userAccountRelationRepository.findAllByAccountId(account.getAccountId()));

                    Long userId = userAccountRelationList.stream()
                            .filter(relation -> relation.getUserRole() == UserRole.ADMIN)
                            .map(UserAccountRelation::getUserId)
                            .findFirst()
                            .orElseThrow(UserAccountRelationNotFoundException::getInstance);

                    // User 서버에 userName 반환 요청
                    UserNameResponseDTO userNameResponseDTO = userClient.getUserName(userId);

                    // User 서버에 userKey 반환 요청
                    UserKeyRequestDTO userKeyRequestDTO = UserKeyRequestDTO.builder()
                            .userId(userId)
                            .build();
                    UserKeyResponseDTO userKeyResponseDTO = userClient.getUserKey(userKeyRequestDTO);

                    // SSAFY 금융 API 계좌 조회 (단건) 요청
                    InquireDemandDepositAccountRequestDTO inquireDemandDepositAccountRequestDTO = InquireDemandDepositAccountRequestDTO.builder()
                            .header(RequestHeader.builder()
                                    .apiName("inquireDemandDepositAccount")
                                    .userKey(userKeyResponseDTO.getUserKey())
                                    .build())
                            .accountNo(account.getAccountNo())
                            .build();
                    InquireDemandDepositAccountResponseDTO inquireDemandDepositAccountResponseDTO = domesticClient.inquireDemandDepositAccount(inquireDemandDepositAccountRequestDTO);

                    return RecentTransactionListResponseDTO.Info.builder()
                            .accountId(account.getAccountId())
                            .accountNo(transactionHistory.getTransactionAccountNo())
                            .adminName(userNameResponseDTO.getUserName())
                            .bankName(inquireDemandDepositAccountResponseDTO.getRec().getBankName())
                            .build();
                })
                .distinct()
                .limit(6)
                .collect(Collectors.toList());

        return RecentTransactionListResponseDTO.builder()
                .accountList(accountList)
                .build();
    }

    // 계좌 이체 한도 변경 서비스 로직
    public SsafySuccessResponseDTO updateTransferLimit(Long accountId, UpdateAccountTransferLimitRequestDTO requestDTO) {

        String accountNo = ValidationUtil.validateInput(ValidateInputDTO.builder()
                        .account(accountRepository.findById(accountId))
                        .userAccountRelation(userAccountRelationRepository.findByUserIdAndAccountId(UserHeaderInterceptor.userId.get(), accountId))
                        .userRole(UserRole.ADMIN)
                        .build())
                .getAccountNo();

        // SSAFY 금융 API 계좌 이체 한도 변경 요청
        UpdateTransferLimitRequestDTO updateTransferLimitRequestDTO = UpdateTransferLimitRequestDTO.builder()
                .header(RequestHeader.builder()
                        .apiName("updateTransferLimit")
                        .userKey(UserHeaderInterceptor.userKey.get())
                        .build())
                .accountNo(accountNo)
                .oneTimeTransferLimit(requestDTO.getOneTimeTransferLimit())
                .dailyTransferLimit(requestDTO.getDailyTransferLimit())
                .build();
        UpdateTransferLimitResponseDTO updateTransferLimitResponseDTO = domesticClient.updateTransferLimit(updateTransferLimitRequestDTO);

        return SsafySuccessResponseDTO.builder()
                .responseCode(updateTransferLimitResponseDTO.getHeader().getResponseCode())
                .responseMessage(updateTransferLimitResponseDTO.getHeader().getResponseMessage())
                .build();
    }

    // 개인 통장 생성 서비스 로직
    public SsafySuccessResponseDTO createPersonalAccount(CreatePersonalAccountRequestDTO requestDTO) {

        // SSAFY 금융 API 계좌 생성 요청
        CreateDemandDepositAccountRequestDTO createDemandDepositAccountRequestDTO = CreateDemandDepositAccountRequestDTO.builder()
                .header(RequestHeader.builder()
                        .apiName("createDemandDepositAccount")
                        .userKey(UserHeaderInterceptor.userKey.get())
                        .build())
                .accountTypeUniqueNo(PERSONAL_ACCOUNT_TYPE_UNIQUE_NO)
                .build();
        CreateDemandDepositAccountResponseDTO createDemandDepositAccountResponseDTO = domesticClient.createDemandDepositAccount(createDemandDepositAccountRequestDTO);

        String accountNo = createDemandDepositAccountResponseDTO.getRec().getAccountNo();
        String hashedPassword = passwordEncoder.encode(requestDTO.getPassword() + PEPPER);

        // Account 테이블에 저장
        Account account = Account.builder()
                .accountNo(accountNo)
                .password(hashedPassword)
                .accountType(AccountType.PERSONAL)
                .build();
        Account savedAccount = accountRepository.save(account);

        // UserAccountRelation 테이블에 저장
        UserAccountRelation userAccountRelation = UserAccountRelation.builder()
                .userId(UserHeaderInterceptor.userId.get())
                .account(savedAccount)
                .userRole(UserRole.ADMIN)
                .build();
        userAccountRelationRepository.save(userAccountRelation);

        // User 서버에 mainAccountId가 존재하는지 조회해서 존재 안하면 mainAccount로 저장
        if (userClient.getMainAccountId(UserHeaderInterceptor.userId.get()).getMainAccountId() == null) {
            MainAccountIdRequestDTO mainAccountIdRequestDTO = MainAccountIdRequestDTO.builder()
                    .userId(UserHeaderInterceptor.userId.get())
                    .mainAccountId(savedAccount.getAccountId())
                    .build();
            userClient.updateMainAccountId(mainAccountIdRequestDTO);
        }

        return SsafySuccessResponseDTO.builder()
                .responseCode(createDemandDepositAccountResponseDTO.getHeader().getResponseCode())
                .responseMessage(createDemandDepositAccountResponseDTO.getHeader().getResponseMessage())
                .build();
    }

    // 개인 통장 상세 조회 서비스 로직
    public PersonalAccountDetailResponseDTO getPersonalAccountDetail(Long accountId, String startDate, String endDate, String transactionType) {

        String accountNo = ValidationUtil.validateInput(ValidateInputDTO.builder()
                    .account(accountRepository.findById(accountId))
                    .userAccountRelation(userAccountRelationRepository.findByUserIdAndAccountId(UserHeaderInterceptor.userId.get(), accountId))
                    .accountType(AccountType.PERSONAL)
                    .userRole(UserRole.ADMIN)
                    .build())
                .getAccountNo();

        // SSAFY 금융 API 계좌 조회 (단건) 요청
        InquireDemandDepositAccountRequestDTO inquireDemandDepositAccountRequestDTO = InquireDemandDepositAccountRequestDTO.builder()
                .header(RequestHeader.builder()
                        .apiName("inquireDemandDepositAccount")
                        .userKey(UserHeaderInterceptor.userKey.get())
                        .build())
                .accountNo(accountNo)
                .build();
        InquireDemandDepositAccountResponseDTO inquireDemandDepositAccountResponseDTO = domesticClient.inquireDemandDepositAccount(inquireDemandDepositAccountRequestDTO);

        // SSAFY 금융 API 계좌 거래 내역 조회 요청
        InquireTransactionHistoryListRequestDTO inquireTransactionHistoryListRequestDTO = InquireTransactionHistoryListRequestDTO.builder()
                .header(RequestHeader.builder()
                        .apiName("inquireTransactionHistoryList")
                        .userKey(UserHeaderInterceptor.userKey.get())
                        .build())
                .accountNo(accountNo)
                .startDate(startDate)
                .endDate(endDate)
                .transactionType(transactionType)
                .orderByType("DESC")
                .build();
        InquireTransactionHistoryListResponseDTO inquireTransactionHistoryListResponseDTO = domesticClient.inquireTransactionHistoryList(inquireTransactionHistoryListRequestDTO);

        return PersonalAccountDetailResponseDTO.builder()
                .accountId(accountId)
                .accountNo(accountNo)
                .accountBalance(inquireDemandDepositAccountResponseDTO.getRec().getAccountBalance())
                .accountName("개인 입출금 통장")
                .bankName(inquireDemandDepositAccountResponseDTO.getRec().getBankName())
                .transactionList(getPersonalAccountTransactionList(inquireTransactionHistoryListResponseDTO))
                .build();
    }

    // 개인 통장 생성일 조회 서비스 로직
    public PersonalAccountCreatedDateResponseDTO getPersonalAccountCreatedDate(Long accountId) {

        String accountNo = ValidationUtil.validateInput(ValidateInputDTO.builder()
                        .account(accountRepository.findById(accountId))
                        .userAccountRelation(userAccountRelationRepository.findByUserIdAndAccountId(UserHeaderInterceptor.userId.get(), accountId))
                        .accountType(AccountType.PERSONAL)
                        .build())
                .getAccountNo();

        // SSAFY 금융 API 계좌 조회 (단건) 요청
        InquireDemandDepositAccountRequestDTO inquireDemandDepositAccountRequestDTO = InquireDemandDepositAccountRequestDTO.builder()
                .header(RequestHeader.builder()
                        .apiName("inquireDemandDepositAccount")
                        .userKey(UserHeaderInterceptor.userKey.get())
                        .build())
                .accountNo(accountNo)
                .build();
        InquireDemandDepositAccountResponseDTO inquireDemandDepositAccountResponseDTO = domesticClient.inquireDemandDepositAccount(inquireDemandDepositAccountRequestDTO);

        return PersonalAccountCreatedDateResponseDTO.builder()
                .accountCreatedDate(inquireDemandDepositAccountResponseDTO.getRec().getAccountCreatedDate())
                .build();
    }

    // SSAFY 금융 API 계좌 거래 내역 responseDTO -> 개인 통장 거래 내역 리스트
    private List<PersonalAccountDetailResponseDTO.Transaction> getPersonalAccountTransactionList(InquireTransactionHistoryListResponseDTO inquireTransactionHistoryListResponseDTO) {
        return inquireTransactionHistoryListResponseDTO.getRec().getList().stream()
                .map(transaction -> PersonalAccountDetailResponseDTO.Transaction.builder()
                        .transactionType(transaction.getTransactionType())
                        .transactionSummary(transaction.getTransactionSummary())
                        .transactionDate(transaction.getTransactionDate())
                        .transactionTime(transaction.getTransactionTime())
                        .transactionBalance(transaction.getTransactionBalance())
                        .transactionAfterBalance(transaction.getTransactionAfterBalance())
                        .transactionMemo(transaction.getTransactionMemo())
                        .build())
                .collect(Collectors.toList());
    }

    // 개인 통장 계좌 이체 서비스 로직
    public SsafySuccessResponseDTO transferPersonalAccount(Long accountId, TransferPersonalAccountRequestDTO requestDTO) {

        ValidationUtil.validateInput(ValidateInputDTO.builder()
                .account(accountRepository.findById(accountId))
                .userAccountRelation(userAccountRelationRepository.findByUserIdAndAccountId(UserHeaderInterceptor.userId.get(), accountId))
                .accountType(AccountType.PERSONAL)
                .userRole(UserRole.ADMIN)
                .isPayable(true)
                .build());

        String depositTransactionSummary = getAccountName(requestDTO.getWithdrawalAccountNo()).getName();
        String withdrawalTransactionSummary = getAccountName(requestDTO.getDepositAccountNo()).getName();

        // SSAFY 금융 API 계좌 이체 요청
        UpdateDemandDepositAccountTransferRequestDTO updateDemandDepositAccountTransferRequestDTO = UpdateDemandDepositAccountTransferRequestDTO.builder()
                .header(RequestHeader.builder()
                        .apiName("updateDemandDepositAccountTransfer")
                        .userKey(UserHeaderInterceptor.userKey.get())
                        .build())
                .depositAccountNo(requestDTO.getDepositAccountNo())
                .depositTransactionSummary(depositTransactionSummary)
                .transactionBalance(requestDTO.getTransactionBalance())
                .withdrawalAccountNo(requestDTO.getWithdrawalAccountNo())
                .withdrawalTransactionSummary(withdrawalTransactionSummary)
                .build();
        UpdateDemandDepositAccountTransferResponseDTO updateDemandDepositAccountTransferResponseDTO = domesticClient.updateDemandDepositAccountTransfer(updateDemandDepositAccountTransferRequestDTO);

        Long depositAccountId = ValidationUtil.validateAccount(accountRepository.findByAccountNo(requestDTO.getDepositAccountNo())).getAccountId();
        List<UserAccountRelation> userAccountRelationList = ValidationUtil.validateUserAccountRelationList(userAccountRelationRepository.findAllByAccountId(depositAccountId));

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

        // SSAFY 금융 API 거래내역 메모 요청
        TransactionMemoRequestDTO transactionMemoRequestDTO = TransactionMemoRequestDTO.builder()
                .header(RequestHeader.builder()
                        .apiName("transactionMemo")
                        .userKey(userKeyResponseDTO.getUserKey())
                        .build())
                .accountNo(requestDTO.getDepositAccountNo())
                .transactionUniqueNo(updateDemandDepositAccountTransferResponseDTO.getRec().get(1).getTransactionUniqueNo())
                .transactionMemo(String.valueOf(UserHeaderInterceptor.userId.get()))
                .build();
        memoClient.transactionMeno(transactionMemoRequestDTO);

        return SsafySuccessResponseDTO.builder()
                .responseCode(updateDemandDepositAccountTransferResponseDTO.getHeader().getResponseCode())
                .responseMessage(updateDemandDepositAccountTransferResponseDTO.getHeader().getResponseMessage())
                .build();
    }

    // 개인 통장 계좌 이체 비밀번호 검증 API
    public InternalServerSuccessResponseDTO verifyPersonalAccountPassword(Long accountId, VerifyAccountPasswordRequestDTO requestDTO) {

        String savedPassword = ValidationUtil.validateInput(ValidateInputDTO.builder()
                        .account(accountRepository.findById(accountId))
                        .userAccountRelation(userAccountRelationRepository.findByUserIdAndAccountId(UserHeaderInterceptor.userId.get(), accountId))
                        .accountType(AccountType.PERSONAL)
                        .userRole(UserRole.ADMIN)
                        .isPayable(true)
                        .build())
                .getPassword();

        if (!passwordEncoder.matches(requestDTO.getPassword() + PEPPER, savedPassword)) {
            throw InvalidPasswordException.getInstance();
        }

        return InternalServerSuccessResponseDTO.builder()
                .message("통장 비밀번호 검증 성공")
                .build();
    }

    // 한화 여행통장 생성 서비스 로직
    public SsafySuccessResponseDTO createDomesticTravelAccount(CreateDomesticTravelAccountRequestDTO requestDTO) {

        // SSAFY 금융 API 계좌 생성 요청
        CreateDemandDepositAccountRequestDTO createDemandDepositAccountRequestDTO = CreateDemandDepositAccountRequestDTO.builder()
                .header(RequestHeader.builder()
                        .apiName("createDemandDepositAccount")
                        .userKey(UserHeaderInterceptor.userKey.get())
                        .build())
                .accountTypeUniqueNo(DOMESTIC_TRAVEL_ACCOUNT_TYPE_UNIQUE_NO)
                .build();
        CreateDemandDepositAccountResponseDTO createDemandDepositAccountResponseDTO = domesticClient.createDemandDepositAccount(createDemandDepositAccountRequestDTO);

        String accountNo = createDemandDepositAccountResponseDTO.getRec().getAccountNo();
        String hashedPassword = passwordEncoder.encode(requestDTO.getPassword() + PEPPER);

        // Account 테이블에 저장
        Account account = Account.builder()
                .accountNo(accountNo)
                .password(hashedPassword)
                .accountType(AccountType.DOMESTIC)
                .build();
        Account savedAccount = accountRepository.save(account);

        // UserAccountRelation 테이블에 저장
        UserAccountRelation userAccountRelation = UserAccountRelation.builder()
                .userId(UserHeaderInterceptor.userId.get())
                .account(savedAccount)
                .userRole(UserRole.ADMIN)
                .build();
        userAccountRelationRepository.save(userAccountRelation);

        // Travel 서버에 원화계좌 생성시 원화여행계좌 테이블에 정보 저장 요청
        SaveDomesticTravelAccountRequestDTO saveDomesticTravelAccountRequestDTO = SaveDomesticTravelAccountRequestDTO.builder()
                .accountId(savedAccount.getAccountId())
                .accountName(requestDTO.getAccountName())
                .targetAmount(requestDTO.getTargetAmount())
                .build();
        travelClient.saveDomesticTravelAccount(saveDomesticTravelAccountRequestDTO);

        return SsafySuccessResponseDTO.builder()
                .responseCode(createDemandDepositAccountResponseDTO.getHeader().getResponseCode())
                .responseMessage(createDemandDepositAccountResponseDTO.getHeader().getResponseMessage())
                .build();
    }

    // 한화 여행통장 상세 조회 서비스 로직
    public DomesticTravelAccountDetailResponseDTO getDomesticTravelAccountDetail(Long accountId, String startDate, String endDate, String transactionType, String selectedUserId) {

        UserRole userRole = ValidationUtil.validateUserAccountRelation(userAccountRelationRepository.findByUserIdAndAccountId(UserHeaderInterceptor.userId.get(), accountId)).getUserRole();

        String accountNo = ValidationUtil.validateInput(ValidateInputDTO.builder()
                        .account(accountRepository.findById(accountId))
                        .userAccountRelation(userAccountRelationRepository.findByUserIdAndAccountId(UserHeaderInterceptor.userId.get(), accountId))
                        .accountType(AccountType.DOMESTIC)
                        .build())
                .getAccountNo();

        // Travel 서버에 한화 여행통장 ID로 이름과 목표금액 반환 요청
        DomesticTravelAccountInfoResponseDTO domesticTravelAccountInfo = travelClient.getDomesticTravelAccountInfo(accountId);

        String ssafyUserKey;

        if (userRole == UserRole.ADMIN) {
            ssafyUserKey = UserHeaderInterceptor.userKey.get();
        }
        else {
            List<UserAccountRelation> userAccountRelationList = ValidationUtil.validateUserAccountRelationList(userAccountRelationRepository.findAllByAccountId(accountId));

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

            ssafyUserKey = userKeyResponseDTO.getUserKey();
        }

        // SSAFY 금융 API 계좌 조회 (단건) 요청
        InquireDemandDepositAccountRequestDTO inquireDemandDepositAccountRequestDTO = InquireDemandDepositAccountRequestDTO.builder()
                .header(RequestHeader.builder()
                        .apiName("inquireDemandDepositAccount")
                        .userKey(ssafyUserKey)
                        .build())
                .accountNo(accountNo)
                .build();
        InquireDemandDepositAccountResponseDTO inquireDemandDepositAccountResponseDTO = domesticClient.inquireDemandDepositAccount(inquireDemandDepositAccountRequestDTO);

        // SSAFY 금융 API 계좌 거래 내역 조회 요청
        InquireTransactionHistoryListRequestDTO inquireTransactionHistoryListRequestDTO = InquireTransactionHistoryListRequestDTO.builder()
                .header(RequestHeader.builder()
                        .apiName("inquireTransactionHistoryList")
                        .userKey(ssafyUserKey)
                        .build())
                .accountNo(accountNo)
                .startDate(startDate)
                .endDate(endDate)
                .transactionType(transactionType)
                .orderByType("DESC")
                .build();
        InquireTransactionHistoryListResponseDTO inquireTransactionHistoryListResponseDTO = domesticClient.inquireTransactionHistoryList(inquireTransactionHistoryListRequestDTO);

        return DomesticTravelAccountDetailResponseDTO.builder()
                .accountName(domesticTravelAccountInfo.getAccountName())
                .targetAmount(domesticTravelAccountInfo.getTargetAmount())
                .accountId(accountId)
                .accountNo(accountNo)
                .accountBalance(inquireDemandDepositAccountResponseDTO.getRec().getAccountBalance())
                .bankName(inquireDemandDepositAccountResponseDTO.getRec().getBankName())
                .transactionList(getDomesticTravelAccountTransactionList(inquireTransactionHistoryListResponseDTO, selectedUserId))
                .build();
    }

//    // SSAFY 금융 API 계좌 거래 내역 responseDTO -> 한화 여행 통장 거래 내역 리스트
//    private List<DomesticTravelAccountDetailResponseDTO.Transaction> getDomesticTravelAccountTransactionList(InquireTransactionHistoryListResponseDTO inquireTransactionHistoryListResponseDTO, Long selectedUserId) {
//        return inquireTransactionHistoryListResponseDTO.getRec().getList().stream()
//                .map(transaction -> DomesticTravelAccountDetailResponseDTO.Transaction.builder()
//                        .transactionType(transaction.getTransactionType())
//                        .transactionSummary(transaction.getTransactionSummary())
//                        .transactionDate(transaction.getTransactionDate())
//                        .transactionTime(transaction.getTransactionTime())
//                        .transactionBalance(transaction.getTransactionBalance())
//                        .transactionAfterBalance(transaction.getTransactionAfterBalance())
//                        .transactionMemo(transaction.getTransactionMemo())
//                        .build())
//                .collect(Collectors.toList());
//    }

    // SSAFY 금융 API 계좌 거래 내역 responseDTO -> 한화 여행 통장 거래 내역 리스트
    private List<DomesticTravelAccountDetailResponseDTO.Transaction> getDomesticTravelAccountTransactionList(InquireTransactionHistoryListResponseDTO inquireTransactionHistoryListResponseDTO, String selectedUserId) {
        return inquireTransactionHistoryListResponseDTO.getRec().getList().stream()
                .filter(transaction -> {
                    String transactionMemo = transaction.getTransactionMemo();

                    if ("-1".equals(selectedUserId)) {
                        return true;
                    }

                    return selectedUserId.equals(transactionMemo);
                })
                .map(transaction -> DomesticTravelAccountDetailResponseDTO.Transaction.builder()
                        .transactionType(transaction.getTransactionType())
                        .transactionSummary(transaction.getTransactionSummary())
                        .transactionDate(transaction.getTransactionDate())
                        .transactionTime(transaction.getTransactionTime())
                        .transactionBalance(transaction.getTransactionBalance())
                        .transactionAfterBalance(transaction.getTransactionAfterBalance())
                        .transactionMemo(transaction.getTransactionMemo())
                        .build())
                .collect(Collectors.toList());
    }

    // 한화 여행통장 생성일 조회 서비스 로직
    public DomesticTravelAccountCreatedDateResponseDTO getDomesticTravelAccountCreatedDate(Long accountId) {

        String accountNo = ValidationUtil.validateInput(ValidateInputDTO.builder()
                        .account(accountRepository.findById(accountId))
                        .userAccountRelation(userAccountRelationRepository.findByUserIdAndAccountId(UserHeaderInterceptor.userId.get(), accountId))
                        .accountType(AccountType.DOMESTIC)
                        .build())
                .getAccountNo();

        // SSAFY 금융 API 계좌 조회 (단건) 요청
        InquireDemandDepositAccountRequestDTO inquireDemandDepositAccountRequestDTO = InquireDemandDepositAccountRequestDTO.builder()
                .header(RequestHeader.builder()
                        .apiName("inquireDemandDepositAccount")
                        .userKey(UserHeaderInterceptor.userKey.get())
                        .build())
                .accountNo(accountNo)
                .build();
        InquireDemandDepositAccountResponseDTO inquireDemandDepositAccountResponseDTO = domesticClient.inquireDemandDepositAccount(inquireDemandDepositAccountRequestDTO);

        return DomesticTravelAccountCreatedDateResponseDTO.builder()
                .accountCreatedDate(inquireDemandDepositAccountResponseDTO.getRec().getAccountCreatedDate())
                .build();
    }

    // 한화 여행통장 잔액 조회 서비스 로직
    public DomesticTravelAccountBalanceResponseDTO getDomesticTravelAccountBalance(Long accountId) {

        String accountNo = ValidationUtil.validateInput(ValidateInputDTO.builder()
                        .account(accountRepository.findById(accountId))
                        .userAccountRelation(userAccountRelationRepository.findByUserIdAndAccountId(UserHeaderInterceptor.userId.get(), accountId))
                        .accountType(AccountType.DOMESTIC)
                        .build())
                .getAccountNo();

        // SSAFY 금융 API 계좌 잔액 조회 요청
        InquireDemandDepositAccountBalanceRequestDTO inquireDemandDepositAccountBalanceRequestDTO = InquireDemandDepositAccountBalanceRequestDTO.builder()
                .header(RequestHeader.builder()
                        .apiName("inquireDemandDepositAccountBalance")
                        .userKey(UserHeaderInterceptor.userKey.get())
                        .build())
                .accountNo(accountNo)
                .build();
        InquireDemandDepositAccountBalanceResponseDTO inquireDemandDepositAccountBalanceResponseDTO = domesticClient.inquireDemandDepositAccountBalance(inquireDemandDepositAccountBalanceRequestDTO);

        return DomesticTravelAccountBalanceResponseDTO.builder()
                .accountBalance(inquireDemandDepositAccountBalanceResponseDTO.getRec().getAccountBalance())
                .build();
    }

    // 한화 여행통장 계좌 이체 서비스 로직
    public SsafySuccessResponseDTO transferDomesticTravelAccount(Long accountId, TransferDomesticTravelAccountRequestDTO requestDTO) {

        ValidationUtil.validateInput(ValidateInputDTO.builder()
                .account(accountRepository.findById(accountId))
                .userAccountRelation(userAccountRelationRepository.findByUserIdAndAccountId(UserHeaderInterceptor.userId.get(), accountId))
                .accountType(AccountType.DOMESTIC)
                .isPayable(true)
                .build());

        String depositTransactionSummary = getAccountName(requestDTO.getWithdrawalAccountNo()).getName();
        String withdrawalTransactionSummary = getAccountName(requestDTO.getDepositAccountNo()).getName();

        // SSAFY 금융 API 계좌 이체 요청
        UpdateDemandDepositAccountTransferRequestDTO updateDemandDepositAccountTransferRequestDTO = UpdateDemandDepositAccountTransferRequestDTO.builder()
                .header(RequestHeader.builder()
                        .apiName("updateDemandDepositAccountTransfer")
                        .userKey(UserHeaderInterceptor.userKey.get())
                        .build())
                .depositAccountNo(requestDTO.getDepositAccountNo())
                .depositTransactionSummary(depositTransactionSummary)
                .transactionBalance(requestDTO.getTransactionBalance())
                .withdrawalAccountNo(requestDTO.getWithdrawalAccountNo())
                .withdrawalTransactionSummary(withdrawalTransactionSummary)
                .build();
        UpdateDemandDepositAccountTransferResponseDTO updateDemandDepositAccountTransferResponseDTO = domesticClient.updateDemandDepositAccountTransfer(updateDemandDepositAccountTransferRequestDTO);

        Long depositAccountId = ValidationUtil.validateAccount(accountRepository.findByAccountNo(requestDTO.getDepositAccountNo())).getAccountId();
        List<UserAccountRelation> userAccountRelationList = ValidationUtil.validateUserAccountRelationList(userAccountRelationRepository.findAllByAccountId(depositAccountId));

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

        // SSAFY 금융 API 거래내역 메모 요청
        TransactionMemoRequestDTO transactionMemoRequestDTO = TransactionMemoRequestDTO.builder()
                .header(RequestHeader.builder()
                        .apiName("transactionMemo")
                        .userKey(userKeyResponseDTO.getUserKey())
                        .build())
                .accountNo(requestDTO.getDepositAccountNo())
                .transactionUniqueNo(updateDemandDepositAccountTransferResponseDTO.getRec().get(1).getTransactionUniqueNo())
                .transactionMemo(String.valueOf(UserHeaderInterceptor.userId.get()))
                .build();
        memoClient.transactionMeno(transactionMemoRequestDTO);

        return SsafySuccessResponseDTO.builder()
                .responseCode(updateDemandDepositAccountTransferResponseDTO.getHeader().getResponseCode())
                .responseMessage(updateDemandDepositAccountTransferResponseDTO.getHeader().getResponseMessage())
                .build();
    }

    // 한화 여행통장 계좌 이체 비밀번호 검증 API
    public InternalServerSuccessResponseDTO verifyDomesticTravelAccountPassword(Long accountId, VerifyAccountPasswordRequestDTO requestDTO) {

        String savedPassword = ValidationUtil.validateInput(ValidateInputDTO.builder()
                        .account(accountRepository.findById(accountId))
                        .userAccountRelation(userAccountRelationRepository.findByUserIdAndAccountId(UserHeaderInterceptor.userId.get(), accountId))
                        .accountType(AccountType.DOMESTIC)
                        .isPayable(true)
                        .build())
                .getPassword();

        if (!passwordEncoder.matches(requestDTO.getPassword() + PEPPER, savedPassword)) {
            throw InvalidPasswordException.getInstance();
        }

        return InternalServerSuccessResponseDTO.builder()
                .message("통장 비밀번호 검증 성공")
                .build();
    }

    // 한화 여행통장 멤버 목록 조회 서비스 로직 (민채)
    public DomesticTravelAccountMemberListResponseDTO getDomesticTravelAccountMemberList(Long accountId) {
        ValidationUtil.validateInput(ValidateInputDTO.builder()
                .account(accountRepository.findById(accountId))
                .userAccountRelation(userAccountRelationRepository.findByUserIdAndAccountId(UserHeaderInterceptor.userId.get(), accountId))
                .accountType(AccountType.DOMESTIC)
                .build());

        List<UserAccountRelation> userAccountRelations = ValidationUtil.validateUserAccountRelationList(userAccountRelationRepository.findAllByAccountId(accountId));

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

        return DomesticTravelAccountMemberListResponseDTO.builder()
                .userId(UserHeaderInterceptor.userId.get())
                .memberCount((long) members.size())
                .members(members).build();
    }

    // 통장 권한 조회 서비스 로직
    public DomesticTravelAccountUserRoleResponseDTO getDomesticTravelAccountUserRole(Long accountId) {

        UserRole userRole = ValidationUtil.validateUserAccountRelation(userAccountRelationRepository.findByUserIdAndAccountId(UserHeaderInterceptor.userId.get(), accountId)).getUserRole();

        return DomesticTravelAccountUserRoleResponseDTO.builder()
                .userRole(userRole)
                .build();
    }

    // 외화 여행통장 생성 서비스 로직
    public CreateForeignTravelAccountResponseDTO createForeignTravelAccount(CreateForeignTravelAccountRequestDTO requestDTO) {

        String savedPassword = ValidationUtil.validateInput(ValidateInputDTO.builder()
                        .account(accountRepository.findById(requestDTO.getDomesticAccountId()))
                        .userAccountRelation(userAccountRelationRepository.findByUserIdAndAccountId(UserHeaderInterceptor.userId.get(), requestDTO.getDomesticAccountId()))
                        .accountType(AccountType.DOMESTIC)
                        .userRole(UserRole.ADMIN)
                        .build())
                .getPassword();

        // 한화 여행통장 정보 반환용
        Account domesticAccount = ValidationUtil.validateAccount(accountRepository.findById(requestDTO.getDomesticAccountId()));

        // SSAFY 금융 API 계좌 생성 요청
        CreateForeignCurrencyDemandDepositAccountRequestDTO createForeignCurrencyDemandDepositAccountRequestDTO = CreateForeignCurrencyDemandDepositAccountRequestDTO.builder()
                .header(RequestHeader.builder()
                        .apiName("createForeignCurrencyDemandDepositAccount")
                        .userKey(UserHeaderInterceptor.userKey.get())
                        .build())
                .accountTypeUniqueNo(FOREIGN_TRAVEL_ACCOUNT_TYPE_UNIQUE_NO)
                .currency(requestDTO.getCurrency())
                .build();
        CreateForeignCurrencyDemandDepositAccountResponseDTO createForeignCurrencyDemandDepositAccountResponseDTO = foreignClient.createForeignCurrencyDemandDepositAccount(createForeignCurrencyDemandDepositAccountRequestDTO);

        String accountNo = createForeignCurrencyDemandDepositAccountResponseDTO.getRec().getAccountNo();
        String currency = createForeignCurrencyDemandDepositAccountResponseDTO.getRec().getCurrency().getCurrency();

        // Account 테이블에 저장
        Account account = Account.builder()
                .accountNo(accountNo)
                .password(savedPassword)
                .accountType(AccountType.FOREIGN)
                .build();
        Account savedAccount = accountRepository.save(account);

        // UserAccountRelation 테이블에 저장
        UserAccountRelation userAccountRelation = UserAccountRelation.builder()
                .userId(UserHeaderInterceptor.userId.get())
                .account(savedAccount)
                .userRole(UserRole.ADMIN)
                .build();
        userAccountRelationRepository.save(userAccountRelation);

        // Travel 서버에 외화계좌 생성시 외화여행계좌 테이블에 정보 저장 요청
        SaveForeignTravelAccountRequestDTO saveForeignTravelAccountRequestDTO = SaveForeignTravelAccountRequestDTO.builder()
                .foreignAccountId(savedAccount.getAccountId())
                .domesticAccountId(requestDTO.getDomesticAccountId())
                .currency(currency)
                .build();
        travelClient.saveForeignTravelAccount(saveForeignTravelAccountRequestDTO);

        return CreateForeignTravelAccountResponseDTO.builder()
                .domesticAccountId(domesticAccount.getAccountId())
                .domesticAccountNo(domesticAccount.getAccountNo())
                .foreignAccountId(savedAccount.getAccountId())
                .foreignAccountNo(savedAccount.getAccountNo())
                .build();
    }

    // 외화 여행통장 생성일 조회 서비스 로직
    public ForeignTravelAccountCreatedDateResponseDTO getForeignTravelAccountCreatedDate(Long accountId) {

        String accountNo = ValidationUtil.validateInput(ValidateInputDTO.builder()
                        .account(accountRepository.findById(accountId))
                        .userAccountRelation(userAccountRelationRepository.findByUserIdAndAccountId(UserHeaderInterceptor.userId.get(), accountId))
                        .accountType(AccountType.FOREIGN)
                        .build())
                .getAccountNo();

        // SSAFY 금융 API 외화 계좌 조회 (단건) 요청
        InquireForeignCurrencyDemandDepositAccountRequestDTO inquireForeignCurrencyDemandDepositAccountRequestDTO = InquireForeignCurrencyDemandDepositAccountRequestDTO.builder()
                .header(RequestHeader.builder()
                        .apiName("inquireForeignCurrencyDemandDepositAccount")
                        .userKey(UserHeaderInterceptor.userKey.get())
                        .build())
                .accountNo(accountNo)
                .build();
        InquireForeignCurrencyDemandDepositAccountResponseDTO inquireForeignCurrencyDemandDepositAccountResponseDTO = foreignClient.inquireForeignCurrencyDemandDepositAccount(inquireForeignCurrencyDemandDepositAccountRequestDTO);

        return ForeignTravelAccountCreatedDateResponseDTO.builder()
                .accountCreatedDate(inquireForeignCurrencyDemandDepositAccountResponseDTO.getRec().getAccountCreatedDate())
                .build();
    }

    // 외화 여행통장 및 연결된 한화 여행통장 식별자와 계좌번호 조회 서비스 로직
    public TravelAccountCoupleResponseDTO getTravelAccountCoupleResponseDTO(Long foreignAccountId) {

        String foreignAccountNo = ValidationUtil.validateInput(ValidateInputDTO.builder()
                    .account(accountRepository.findById(foreignAccountId))
                    .userAccountRelation(userAccountRelationRepository.findByUserIdAndAccountId(UserHeaderInterceptor.userId.get(), foreignAccountId))
                    .accountType(AccountType.FOREIGN)
                    .build())
                .getAccountNo();

        // Travel 서버에 외화 여행통장 ID로 한화 여행통장 ID 반환 요청
        Long domesticAccountId = travelClient.getParentAccountId(foreignAccountId).getParentAccountId();

        String domesticAccountNo = ValidationUtil.validateAccount(accountRepository.findById(domesticAccountId)).getAccountNo();

        return TravelAccountCoupleResponseDTO.builder()
                .domesticAccountId(domesticAccountId)
                .domesticAccountNo(domesticAccountNo)
                .foreignAccountId(foreignAccountId)
                .foreignAccountNo(foreignAccountNo)
                .build();
    }

    // 통장 주인 이름 조회 서비스 로직
    public AccountAdminNameResponseDTO getAccountName(String accountNo) {
        Account account = ValidationUtil.validateAccount(accountRepository.findByAccountNo(accountNo));

        if (account.getAccountType() == AccountType.PERSONAL) {
            List<UserAccountRelation> userAccountRelationList = ValidationUtil.validateUserAccountRelationList(userAccountRelationRepository.findAllByAccountId(account.getAccountId()));

            Long userId = userAccountRelationList.stream()
                    .filter(relation -> relation.getUserRole() == UserRole.ADMIN)
                    .map(UserAccountRelation::getUserId)
                    .findFirst()
                    .orElseThrow(UserAccountRelationNotFoundException::getInstance);

            // User 서버에 userName 조회 요청
            String name = userClient.getUserName(userId).getUserName();

            return AccountAdminNameResponseDTO.builder()
                    .name(name)
                    .build();
        }
        else if (account.getAccountType() == AccountType.DOMESTIC) {
            DomesticTravelAccountInfoResponseDTO domesticTravelAccountInfo = travelClient.getDomesticTravelAccountInfo(account.getAccountId());

            String name = domesticTravelAccountInfo.getAccountName();

            return AccountAdminNameResponseDTO.builder()
                    .name(name)
                    .build();
        }
        else if (account.getAccountType() == AccountType.FOREIGN) {
            ParentAccountIdResponseDTO parentAccountIdResponseDTO = travelClient.getParentAccountId(account.getAccountId());

            DomesticTravelAccountInfoResponseDTO domesticTravelAccountInfo = travelClient.getDomesticTravelAccountInfo(parentAccountIdResponseDTO.getParentAccountId());

            String name = domesticTravelAccountInfo.getAccountName();

            return AccountAdminNameResponseDTO.builder()
                    .name(name)
                    .build();
        }
        else {
            throw new InternalServerErrorException("API 문제 발생");
        }
    }

}
