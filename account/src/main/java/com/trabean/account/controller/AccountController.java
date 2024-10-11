package com.trabean.account.controller;

import com.trabean.account.dto.request.*;
import com.trabean.account.dto.response.*;
import com.trabean.account.service.AccountService;
import com.trabean.common.InternalServerSuccessResponseDTO;
import com.trabean.common.SsafySuccessResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    // 통장 목록 조회 APi
    @GetMapping
    public ResponseEntity<AccountListResponseDTO> getAccountList() {
        AccountListResponseDTO responseDTO = accountService.getAccountList();
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    // 최근 이체 목록 조회 APi
    @GetMapping("/{accountId}/transfer")
    public ResponseEntity<RecentTransactionListResponseDTO> getRecentTransactionList(@PathVariable Long accountId,
                                                                                     @RequestParam(defaultValue = "19000101") String startDate,
                                                                                     @RequestParam(defaultValue = "21000101") String endDate) {
        RecentTransactionListResponseDTO responseDTO = accountService.getRecentTransactionList(accountId, startDate, endDate);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    // 계좌 이체 한도 변경 API
    @PutMapping("/{accountId}/transfer")
    public ResponseEntity<SsafySuccessResponseDTO> updateTransferLimit(@PathVariable Long accountId,
                                                                       @RequestBody UpdateAccountTransferLimitRequestDTO requestDTO) {
        SsafySuccessResponseDTO responseDTO = accountService.updateTransferLimit(accountId, requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    // 개인 통장 생성 API
    @PostMapping("/personal")
    public ResponseEntity<SsafySuccessResponseDTO> createPersonalAccount(@RequestBody CreatePersonalAccountRequestDTO requestDTO) {
        SsafySuccessResponseDTO responseDTO = accountService.createPersonalAccount(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    // 개인 통장 상세 조회 API
    @GetMapping("/personal/{accountId}")
    public ResponseEntity<PersonalAccountDetailResponseDTO> getPersonalAccountDetail(@PathVariable Long accountId,
                                                                                     @RequestParam(defaultValue = "19000101") String startDate,
                                                                                     @RequestParam(defaultValue = "21000101") String endDate,
                                                                                     @RequestParam(defaultValue = "A") String transactionType) {
        PersonalAccountDetailResponseDTO responseDTO = accountService.getPersonalAccountDetail(accountId, startDate, endDate, transactionType);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    // 개인 통장 생성일 조회 API
    @GetMapping("/personal/{accountId}/created")
    public ResponseEntity<PersonalAccountCreatedDateResponseDTO> getPersonalAccountCreatedDate(@PathVariable Long accountId) {
        PersonalAccountCreatedDateResponseDTO responseDTO = accountService.getPersonalAccountCreatedDate(accountId);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    // 개인 통장 계좌 이체 API
    @PostMapping("/personal/{accountId}/transfer")
    public ResponseEntity<SsafySuccessResponseDTO> transferPersonalAccount(@PathVariable Long accountId,
                                                                           @RequestBody TransferPersonalAccountRequestDTO requestDTO) {
        SsafySuccessResponseDTO responseDTO = accountService.transferPersonalAccount(accountId, requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    // 개인 통장 계좌 이체 비밀번호 검증 API
    @PostMapping("/personal/{accountId}/verify")
    public ResponseEntity<InternalServerSuccessResponseDTO> verifyPersonalAccountPassword(@PathVariable Long accountId,
                                                                                          @RequestBody VerifyAccountPasswordRequestDTO requestDTO) {
        InternalServerSuccessResponseDTO responseDTO = accountService.verifyPersonalAccountPassword(accountId, requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    // 한화 여행통장 생성 API
    @PostMapping("/travel/domestic")
    public ResponseEntity<SsafySuccessResponseDTO> createDomesticTravelAccount(@RequestBody CreateDomesticTravelAccountRequestDTO requestDTO) {
        SsafySuccessResponseDTO responseDTO = accountService.createDomesticTravelAccount(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    // 한화 여행통장 상세 조회 API
    @GetMapping("/travel/domestic/{accountId}")
    public ResponseEntity<DomesticTravelAccountDetailResponseDTO> getDomesticTravelAccountDetail(@PathVariable Long accountId,
                                                                                                 @RequestParam(defaultValue = "19000101") String startDate,
                                                                                                 @RequestParam(defaultValue = "21000101") String endDate,
                                                                                                 @RequestParam(defaultValue = "A") String transactionType,
                                                                                                 @RequestParam(defaultValue = "-1") String selectedUserId) {
        DomesticTravelAccountDetailResponseDTO responseDTO = accountService.getDomesticTravelAccountDetail(accountId, startDate, endDate, transactionType, selectedUserId);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    // 한화 여행통장 생성일 조회 API
    @GetMapping("/travel/domestic/{accountId}/created")
    public ResponseEntity<DomesticTravelAccountCreatedDateResponseDTO> getDomesticTravelAccountCreatedDate(@PathVariable Long accountId) {
        DomesticTravelAccountCreatedDateResponseDTO responseDTO = accountService.getDomesticTravelAccountCreatedDate(accountId);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    // 한화 여행통장 잔액 조회 API
    @GetMapping("/travel/domestic/{accountId}/accountBalance")
    public ResponseEntity<DomesticTravelAccountBalanceResponseDTO> getDomesticTravelAccountBalance(@PathVariable Long accountId) {
        DomesticTravelAccountBalanceResponseDTO responseDTO = accountService.getDomesticTravelAccountBalance(accountId);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    // 한화 여행통장 계좌 이체 API
    @PostMapping("/travel/domestic/{accountId}/transfer")
    public ResponseEntity<SsafySuccessResponseDTO> transferDomesticTravelAccount(@PathVariable Long accountId,
                                                                                 @RequestBody TransferDomesticTravelAccountRequestDTO requestDTO) {
        SsafySuccessResponseDTO responseDTO = accountService.transferDomesticTravelAccount(accountId, requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    // 한화 여행통장 계좌 이체 비밀번호 검증 API
    @PostMapping("/travel/domestic/{accountId}/verify")
    public ResponseEntity<InternalServerSuccessResponseDTO> verifyDomesticTravelAccountPassword(@PathVariable Long accountId,
                                                                                                @RequestBody VerifyAccountPasswordRequestDTO requestDTO) {
        InternalServerSuccessResponseDTO responseDTO = accountService.verifyDomesticTravelAccountPassword(accountId, requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    // 한화 여행통장 멤버 목록 조회 API (민채)
    @GetMapping("/travel/domestic/{accountId}/members")
    public ResponseEntity<DomesticTravelAccountMemberListResponseDTO> getDomesticTravelAccountMemberList(@PathVariable Long accountId) {
        DomesticTravelAccountMemberListResponseDTO responseDTO = accountService.getDomesticTravelAccountMemberList(accountId);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    // 통장 권한 조회 API
    @GetMapping("/travel/domestic/{accountId}/userRole")
    public ResponseEntity<DomesticTravelAccountUserRoleResponseDTO> getDomesticTravelAccountUserRole(@PathVariable Long accountId) {
        DomesticTravelAccountUserRoleResponseDTO responseDTO = accountService.getDomesticTravelAccountUserRole(accountId);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    // 외화 여행통장 생성 API
    @PostMapping("/travel/foreign")
    public ResponseEntity<CreateForeignTravelAccountResponseDTO> createForeignTravelAccount(@RequestBody CreateForeignTravelAccountRequestDTO requestDTO) {
        CreateForeignTravelAccountResponseDTO responseDTO = accountService.createForeignTravelAccount(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    // 외화 여행통장 생성일 조회 API
    @GetMapping("/travel/foreign/{accountId}/created")
    public ResponseEntity<ForeignTravelAccountCreatedDateResponseDTO> getForeignTravelAccountCreatedDate(@PathVariable Long accountId) {
        ForeignTravelAccountCreatedDateResponseDTO responseDTO = accountService.getForeignTravelAccountCreatedDate(accountId);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    // 외화 여행통장 및 연결된 한화 여행통장 식별자와 계좌번호 조회 API
    @GetMapping("/travel/foreign/{accountId}/couple")
    public ResponseEntity<TravelAccountCoupleResponseDTO> getTravelAccountCoupleResponseDTO(@PathVariable Long accountId) {
        TravelAccountCoupleResponseDTO responseDTO = accountService.getTravelAccountCoupleResponseDTO(accountId);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    // 통장 주인 이름 조회 API
    @GetMapping("/{accountNo}/name")
    public ResponseEntity<AccountAdminNameResponseDTO> getAccountName(@PathVariable String accountNo) {
        AccountAdminNameResponseDTO responseDTO = accountService.getAccountName(accountNo);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
