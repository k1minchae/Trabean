package com.trabean.account.controller;

import com.trabean.account.dto.request.CreateDomesticTravelAccountRequestDTO;
import com.trabean.account.dto.request.CreatePersonalAccountRequestDTO;
import com.trabean.account.dto.request.VerifyAccountPasswordRequestDTO;
import com.trabean.account.dto.response.DomesticTravelAccountDetailResponseDTO;
import com.trabean.account.dto.response.DomesticTravelAccountMemberListResponseDTO;
import com.trabean.account.dto.response.PersonalAccountDetailResponseDTO;
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

    // 개인 통장 생성 API
    @PostMapping("/personal")
    public ResponseEntity<SsafySuccessResponseDTO> createPersonalAccount(@RequestHeader Long userId,
                                                                         @RequestHeader String userKey,
                                                                         @RequestBody CreatePersonalAccountRequestDTO requestDTO) {
        SsafySuccessResponseDTO responseDTO = accountService.createPersonalAccount(userId, userKey, requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    // 한화 여행통장 생성 API
    @PostMapping("/travel/domestic")
    public ResponseEntity<SsafySuccessResponseDTO> createDomesticTravelAccount(@RequestHeader Long userId,
                                                                               @RequestHeader String userKey,
                                                                               @RequestBody CreateDomesticTravelAccountRequestDTO requestDTO) {
        SsafySuccessResponseDTO responseDTO = accountService.createDomesticTravelAccount(userId, userKey, requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    // 개인 통장 상세 조회 API
    @GetMapping("/personal/{accountId}")
    public ResponseEntity<PersonalAccountDetailResponseDTO> getPersonalAccountDetail(@RequestHeader Long userId,
                                                                                     @RequestHeader String userKey,
                                                                                     @PathVariable Long accountId,
                                                                                     @RequestParam(defaultValue = "19000101") String startDate,
                                                                                     @RequestParam(defaultValue = "21000101") String endDate,
                                                                                     @RequestParam(defaultValue = "A") String transactionType) {
        PersonalAccountDetailResponseDTO responseDTO = accountService.getPersonalAccountDetail(userId, userKey, accountId, startDate, endDate, transactionType);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    // 한화 여행통장 상세 조회 API
    @GetMapping("/travel/domestic/{accountId}")
    public ResponseEntity<DomesticTravelAccountDetailResponseDTO> getDomesticTravelAccountDetail(@RequestHeader Long userId,
                                                                                                 @RequestHeader String userKey,
                                                                                                 @PathVariable Long accountId,
                                                                                                 @RequestParam(defaultValue = "19000101") String startDate,
                                                                                                 @RequestParam(defaultValue = "21000101") String endDate,
                                                                                                 @RequestParam(defaultValue = "A") String transactionType) {
        DomesticTravelAccountDetailResponseDTO responseDTO = accountService.getDomesticTravelAccountDetail(userId, userKey, accountId, startDate, endDate, transactionType);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    // 개인 통장 계좌 이체 비밀번호 검증 API
    @PostMapping("/personal/{accountId}/verify")
    public ResponseEntity<InternalServerSuccessResponseDTO> verifyPersonalAccountPassword(@RequestHeader Long userId,
                                                                                          @PathVariable Long accountId,
                                                                                          @RequestBody VerifyAccountPasswordRequestDTO requestDTO) {
        InternalServerSuccessResponseDTO responseDTO = accountService.verifyPersonalAccountPassword(userId, accountId, requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    // 한화 여행통장 계좌 이체 비밀번호 검증 API
    @PostMapping("/travel/domestic/{accountId}/verify")
    public ResponseEntity<InternalServerSuccessResponseDTO> verifyDomesticTravelAccountPassword(@RequestHeader Long userId,
                                                                                                @PathVariable Long accountId,
                                                                                                @RequestBody VerifyAccountPasswordRequestDTO requestDTO) {
        InternalServerSuccessResponseDTO responseDTO = accountService.verifyDomesticTravelAccountPassword(userId, accountId, requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    // 한화 여행통장 멤버 목록 조회 API (민채)
    @GetMapping("/travel/domestic/{accountId}/members")
    public ResponseEntity<DomesticTravelAccountMemberListResponseDTO> getDomesticTravelAccountMemberList(@RequestHeader Long userId,
                                                                                                         @PathVariable Long accountId) {
        DomesticTravelAccountMemberListResponseDTO response = accountService.getDomesticTravelAccountMemberList(userId, accountId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
