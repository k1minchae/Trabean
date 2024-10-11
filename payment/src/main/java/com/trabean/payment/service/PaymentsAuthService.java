package com.trabean.payment.service;

import com.trabean.payment.client.AccountClient;
import com.trabean.payment.dto.response.UserRoleResponse;
import com.trabean.payment.enums.UserRole;
import com.trabean.payment.exception.PaymentsException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class PaymentsAuthService {

    private final AccountClient accountClient;

    public void checkAuthPayment(Long userId, Long accountId) {
        // 유저 권한 확인 API 호출
        String requestBody = String.format("{\"userId\":\"%s\", \"accountId\":%d}", userId, accountId);

        try {
            // API 호출
            UserRoleResponse userRoleResponse = accountClient.getUserRole(requestBody);

            // 유저 권한 확인 후 처리
            if (userRoleResponse == null) {
                throw new PaymentsException("유저 결제 권한을 받아오지 못 했습니다.", HttpStatus.BAD_REQUEST);
            }
            if (userRoleResponse.getUserRole() == UserRole.NONE_PAYER) {
                throw new PaymentsException("권한이 없는 사용자입니다.", HttpStatus.FORBIDDEN);
            }

        } catch (RestClientException e) {
            // RestTemplate에서 발생한 예외 처리
            throw new PaymentsException("권한 조회 외부 API 호출 실패: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
