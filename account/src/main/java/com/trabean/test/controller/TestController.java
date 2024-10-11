package com.trabean.test.controller;

import com.trabean.common.SsafySuccessResponseDTO;
import com.trabean.test.dto.request.DepositRequestDTO;
import com.trabean.test.dto.request.WithdrawalRequestDTO;
import com.trabean.test.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/accounts/test")
public class TestController {

    private final TestService testService;

    // 계좌 입금(테스트용) API
    @PostMapping("/deposit")
    ResponseEntity<SsafySuccessResponseDTO> depositTest(@RequestBody DepositRequestDTO requestDTO) {
        SsafySuccessResponseDTO responseDTO = testService.depositTest(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    // 계좌 출금(테스트용) API
    @PostMapping("/withdrawal")
    ResponseEntity<SsafySuccessResponseDTO> withdrawalTest(@RequestBody WithdrawalRequestDTO requestDTO) {
        SsafySuccessResponseDTO responseDTO = testService.withdrawalTest(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
