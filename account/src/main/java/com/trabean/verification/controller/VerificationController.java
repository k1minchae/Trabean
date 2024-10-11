package com.trabean.verification.controller;
import com.trabean.ssafy.api.response.code.ResponseCode;
import com.trabean.verification.dto.request.AccountVerificationRequestDTO;
import com.trabean.verification.dto.request.OneWonVerificationRequestDTO;
import com.trabean.verification.dto.response.AccountVerificationResponseDTO;
import com.trabean.verification.dto.response.OneWonVerificationResponseDTO;
import com.trabean.verification.service.VerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/accounts/verification")
public class VerificationController {

    private final VerificationService verificationService;

    // 1원 인증(1원 송금) API
    @PostMapping("/account")
    public ResponseEntity<AccountVerificationResponseDTO> getAccountVerification(@RequestBody AccountVerificationRequestDTO requestDTO) {
        AccountVerificationResponseDTO responseDTO = verificationService.getAccountVerification(requestDTO);
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

    // 1원 인증(인증번호검증) API
    @PostMapping("/onewon")
    public ResponseEntity<OneWonVerificationResponseDTO> getOneWonVerification(@RequestBody OneWonVerificationRequestDTO requestDTO) {
        OneWonVerificationResponseDTO responseDTO = verificationService.getOneWonVerification(requestDTO);
        ResponseCode responseCode = responseDTO.getResponseCode();

        switch (responseCode) {
            case H0000 -> {
                return new ResponseEntity<>(responseDTO, HttpStatus.OK);
            }
            case A1003, A1086, A1087, A1088, A1090 -> {
                return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
            }
            default -> {
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }
        }
    }

}
