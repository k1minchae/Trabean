package com.trabean.account.controller;

import com.trabean.account.dto.request.*;
import com.trabean.account.dto.response.*;
import com.trabean.account.service.AccountService;
import com.trabean.exception.AccountNotFoundException;
import com.trabean.exception.InvalidPasswordException;
import com.trabean.exception.InvalidRequestException;
import com.trabean.exception.UserAccountRelationNotFoundException;
import com.trabean.ssafy.api.response.code.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    // 개인 통장 생성 API
    @PostMapping("/personal/new")
    public ResponseEntity<CreatePersonalAccountResponseDTO> createPersonalAccount(@RequestBody CreatePersonalAccountRequestDTO requestDTO) {
        CreatePersonalAccountResponseDTO responseDTO = accountService.createPersonalAccount(requestDTO);
        ResponseCode responseCode = responseDTO.getResponseCode();

        switch (responseCode) {
            case H0000 -> {
                return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
            }
            default -> {
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }
        }
    }

    // 한화 여행통장 생성 API
    @PostMapping("/travel/domestic/new")
    public ResponseEntity<CreateDomesticTravelAccountResponseDTO> createDomesticTravelAccount(@RequestBody CreateDomesticTravelAccountRequestDTO requestDTO) {
        CreateDomesticTravelAccountResponseDTO responseDTO = accountService.createDomesticTravelAccount(requestDTO);
        ResponseCode responseCode = responseDTO.getResponseCode();

        switch (responseCode) {
            case H0000 -> {
                return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
            }
            default -> {
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }
        }
    }

    // 통장 목록 조회 API
    @PostMapping
    public ResponseEntity<AccountListResponseDTO> getAccountList(@RequestBody AccountListRequestDTO requestDTO) {
        AccountListResponseDTO responseDTO = accountService.getAccountList(requestDTO);
        ResponseCode responseCode = responseDTO.getResponseCode();

        switch (responseCode) {
            case H0000 -> {
                return new ResponseEntity<>(responseDTO, HttpStatus.OK);
            }
            default -> {
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }
        }
    }

    // 개인 통장 상세 조회 API
    @PostMapping("/personal")
    public ResponseEntity<PersonalAccountDetailResponseDTO> getAccountDetail(@RequestParam(defaultValue = "19000101") String startDate,
                                                                             @RequestParam(defaultValue = "21000101") String endDate,
                                                                             @RequestParam(defaultValue = "A") String transactionType,
                                                                             @RequestBody PersonalAccountDetailRequestDTO requestDTO){
        PersonalAccountDetailResponseDTO responseDTO = accountService.getAccountDetail(requestDTO, startDate, endDate, transactionType);
        ResponseCode responseCode = responseDTO.getResponseCode();

        switch (responseCode) {
            case H0000 -> {
                return new ResponseEntity<>(responseDTO, HttpStatus.OK);
            }
            case A1003 -> {
                return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
            }
            default -> {
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }
        }
    }

    // 한화 여행통장 상세 조회 API
    @PostMapping("/travel/domestic")
    public ResponseEntity<DomesticTravelAccountDetailResponseDTO> getDomesticTravelAccountDetail(@RequestParam(defaultValue = "19000101") String startDate,
                                                                     @RequestParam(defaultValue = "21000101") String endDate,
                                                                     @RequestParam(defaultValue = "A") String transactionType,
                                                                     @RequestBody DomesticTravelAccountDetailRequestDTO requestDTO){
        DomesticTravelAccountDetailResponseDTO responseDTO = accountService.getDomesticTravelAccountDetail(requestDTO, startDate, endDate, transactionType);
        ResponseCode responseCode = responseDTO.getResponseCode();

        switch (responseCode) {
            case H0000 -> {
                return new ResponseEntity<>(responseDTO, HttpStatus.OK);
            }
            case A1003 -> {
                return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
            }
            default -> {
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }
        }
    }

    // 통장 계좌번호 조회 API
    @PostMapping("/get-account-number")
    public ResponseEntity<?> getAccountNo(@RequestBody AccountNoRequestDTO requestDTO) {
        try {
            AccountNoResponseDTO responseDTO = accountService.getAccountNoById(requestDTO);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (AccountNotFoundException e) {
            return new ResponseEntity<>(Collections.singletonMap("message", e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    // 통장 권환 조회 API
    @PostMapping("/get-user-role")
    public ResponseEntity<?> getUserRole(@RequestBody UserRoleRequestDTO requestDTO) {
        try {
            UserRoleResponseDTO responseDTO = accountService.getUserRoleByUserIdAndAccountId(requestDTO);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (UserAccountRelationNotFoundException e) {
            return new ResponseEntity<>(Collections.singletonMap("message", e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    // 통장 권한 변경 API
    @PostMapping("/update-user-role")
    public ResponseEntity<?> updateUserRole(@RequestBody UpdateUserRoleRequestDTO requestDTO) {
        try {
            UpdateUserRoleResponseDTO responseDTO = accountService.updateUserRole(requestDTO);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (UserAccountRelationNotFoundException e) {
            return new ResponseEntity<>(Collections.singletonMap("message", e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    // 통장 비밀번호 검증 API
    @PostMapping("/verify-password")
    public ResponseEntity<?> verifyPassword(@RequestBody VerifyPasswordRequestDTO requestDTO) {
        try {
            VerifyPasswordResponseDTO responseDTO = accountService.verifyPassword(requestDTO);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (InvalidRequestException e) {
            return new ResponseEntity<>(Collections.singletonMap("message", e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (AccountNotFoundException e) {
            return new ResponseEntity<>(Collections.singletonMap("message", e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (InvalidPasswordException e) {
            return new ResponseEntity<>(Collections.singletonMap("message", e.getMessage()), HttpStatus.UNAUTHORIZED);
        }
    }

}
