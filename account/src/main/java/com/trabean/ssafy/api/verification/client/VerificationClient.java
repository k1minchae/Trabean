package com.trabean.ssafy.api.verification.client;

import com.trabean.ssafy.api.config.FeignClientConfiguration;
import com.trabean.ssafy.api.verification.dto.requestDTO.CheckAuthCodeRequestDTO;
import com.trabean.ssafy.api.verification.dto.requestDTO.OpenAccountAuthRequestDTO;
import com.trabean.ssafy.api.verification.dto.responseDTO.CheckAuthCodeResponseDTO;
import com.trabean.ssafy.api.verification.dto.responseDTO.OpenAccountAuthResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * SSAFY 금융 API p.202 ~ p.207 1원 인증 관련 요청 처리 클라이언트
 */
@FeignClient(name = "verificationClient", url = "https://finopenapi.ssafy.io/ssafy/api/v1/edu/accountAuth", configuration = FeignClientConfiguration.class)
public interface VerificationClient {

    /**
     * SSAFY 금융 API p.202 - 1원 송금
     *
     * @param requestDTO
     * @return
     */
    @PostMapping("/openAccountAuth")
    OpenAccountAuthResponseDTO openAccountAuth(@RequestBody OpenAccountAuthRequestDTO requestDTO);

    /**
     * SSAFY 금융 API p.205 - 1원 송금 검증
     *
     * @param requestDTO
     * @return
     */
    @PostMapping("/checkAuthCode")
    CheckAuthCodeResponseDTO checkAuthCode(@RequestBody CheckAuthCodeRequestDTO requestDTO);
}
