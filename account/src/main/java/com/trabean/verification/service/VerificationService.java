package com.trabean.verification.service;

import com.trabean.ssafy.api.config.CustomFeignClientException;
import com.trabean.ssafy.api.response.code.ResponseCode;
import com.trabean.ssafy.api.verification.client.VerificationClient;
import com.trabean.ssafy.api.verification.dto.requestDTO.CheckAuthCodeRequestDTO;
import com.trabean.ssafy.api.verification.dto.requestDTO.OpenAccountAuthRequestDTO;
import com.trabean.ssafy.api.verification.dto.responseDTO.CheckAuthCodeResponseDTO;
import com.trabean.ssafy.api.verification.dto.responseDTO.OpenAccountAuthResponseDTO;
import com.trabean.util.RequestHeader;
import com.trabean.verification.dto.request.AccountVerificationRequestDTO;
import com.trabean.verification.dto.request.OneWonVerificationRequestDTO;
import com.trabean.verification.dto.response.AccountVerificationResponseDTO;
import com.trabean.verification.dto.response.OneWonVerificationResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerificationService {

    private final VerificationClient verificationClient;

    // 1원 인증(1원 송금) 서비스 로직
    public AccountVerificationResponseDTO getAccountVerification(AccountVerificationRequestDTO requestDTO) {
        String userKey = requestDTO.getUserKey();
        String accountNo = requestDTO.getAccountNo();

        // SSAFY API 1원 송금 요청
        OpenAccountAuthRequestDTO openAccountAuthRequestDTO = OpenAccountAuthRequestDTO.builder()
                .header(RequestHeader.builder()
                        .apiName("openAccountAuth")
                        .userKey(userKey)
                        .build())
                .accountNo(accountNo)
                .build();

        ResponseCode responseCode;
        String responseMessage;

        try {
            OpenAccountAuthResponseDTO openAccountAuthResponseDTO = verificationClient.openAccountAuth(openAccountAuthRequestDTO);

            responseCode = openAccountAuthResponseDTO.getHeader().getResponseCode();
            responseMessage = openAccountAuthResponseDTO.getHeader().getResponseMessage();

        } catch (CustomFeignClientException e) {
            responseCode = e.getErrorResponse().getResponseCode();
            responseMessage = e.getErrorResponse().getResponseMessage();
        }

        return AccountVerificationResponseDTO.builder()
                .responseCode(responseCode)
                .responseMessage(responseMessage)
                .build();
    }

    // 1원 인증(인증번호검증) 서비스 로직
    public OneWonVerificationResponseDTO getOneWonVerification(OneWonVerificationRequestDTO requestDTO) {
        String userKey = requestDTO.getUserKey();
        String accountNo = requestDTO.getAccountNo();
        String otp = requestDTO.getOtp();

        // SSAFY API 1원 송금 검증 요청
        CheckAuthCodeRequestDTO checkAuthCodeRequestDTO = CheckAuthCodeRequestDTO.builder()
                .header(RequestHeader.builder()
                        .apiName("checkAuthCode")
                        .userKey(userKey)
                        .build())
                .accountNo(accountNo)
                .authCode(otp)
                .build();

        ResponseCode responseCode;
        String responseMessage;

        try {
            CheckAuthCodeResponseDTO checkAuthCodeResponseDTO = verificationClient.checkAuthCode(checkAuthCodeRequestDTO);

            responseCode = checkAuthCodeResponseDTO.getHeader().getResponseCode();
            responseMessage = checkAuthCodeResponseDTO.getHeader().getResponseMessage();

        } catch (CustomFeignClientException e) {
            responseCode = e.getErrorResponse().getResponseCode();
            responseMessage = e.getErrorResponse().getResponseMessage();
        }

        return OneWonVerificationResponseDTO.builder()
                .responseCode(responseCode)
                .responseMessage(responseMessage)
                .build();
    }

}
