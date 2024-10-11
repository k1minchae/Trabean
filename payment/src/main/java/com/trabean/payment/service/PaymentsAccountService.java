package com.trabean.payment.service;

import com.trabean.payment.client.AccountClient;
import com.trabean.payment.client.ssafy.DemandDepositClient;
import com.trabean.payment.dto.request.BalanceRequest;
import com.trabean.payment.dto.request.Header;
import com.trabean.payment.dto.request.RequestPaymentRequest;
import com.trabean.payment.dto.response.AccountNoResponse;
import com.trabean.payment.dto.response.BalanceResponse;
import com.trabean.payment.entity.Merchants;
import com.trabean.payment.exception.PaymentsException;
import com.trabean.payment.repository.MerchantsRepository;
import com.trabean.payment.repository.PaymentsRepository;
import com.trabean.payment.util.ApiName;
import feign.FeignException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class PaymentsAccountService {


    private final AccountClient accountClient;
    private final MerchantsRepository merchantsRepository;
    private static final Logger logger = LoggerFactory.getLogger(PaymentsAccountService.class);
    private final DemandDepositClient demandDepositClient;

    // 유저 키 임시 설정
    @Value("${external.key.userKey}")
    private String userKey;

    // 계좌 번호 조회
    public String getAccountNumber(Long accountId) throws PaymentsException {
        String requestBody = String.format("{\"accountId\":\"%s\"}", accountId);

        try {
            // API 호출
            AccountNoResponse response = accountClient.getAccountNumber(requestBody);

            if (response.getAccountNo() == null) {
                throw new PaymentsException("해당 계좌를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST);
            }

            return response.getAccountNo();

        } catch (FeignException e) {
            throw new PaymentsException("계좌 번호 조회 외부 API 호출 실패: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 잔액 조회 후 검증
    public void validateAmount(Long accountId, RequestPaymentRequest requestPaymentRequest) {
        String apiType;
        String accountNo;

        // Merchant 정보 조회
        Merchants merchant = merchantsRepository.findById(requestPaymentRequest.getMerchantId())
                .orElseThrow(() -> new PaymentsException("잘못된 merchantId값 입니다.", HttpStatus.NOT_FOUND));

        // 한화인지 외화인지 구별
        if (merchant.getExchangeCurrency().equals("KRW")) {
            apiType = ApiName.KRW_BALANCE;
        } else {
            apiType = ApiName.FOREIGN_BALANCE;
        }

        accountNo = getAccountNumber(accountId);

        // BalanceRequest 생성
        BalanceRequest balanceRequest = new BalanceRequest(
                Header.builder().apiName(apiType).userKey(userKey).build(),
                accountNo
        );

        try {
            BalanceResponse response;
            if (apiType.equals(ApiName.KRW_BALANCE)) {
                response = demandDepositClient.getKRWBalance(balanceRequest);
            } else {
                response = demandDepositClient.getFORBalance(balanceRequest);
            }

            // 결과 로그 남기기
            if (response != null && response.getRec() != null) {
                logger.info("계좌 잔액 조회: {}", response.getRec().getAccountBalance());

                if (response.getRec().getAccountBalance() < requestPaymentRequest.getForeignAmount()) {
                    logger.info("계좌 잔액 부족");
                    throw new PaymentsException("계좌 잔액이 부족합니다. 현재 잔액: " + response.getRec().getAccountBalance(),
                            HttpStatus.BAD_REQUEST);
                } else {
                    logger.info("계좌 출금 가능");
                }
            } else {
                logger.warn("계좌 잔액 조회 실패");
            }
        } catch (RestClientException e) {
            logger.error("계좌 잔액 조회 API 호출 중 오류 발생: {}", e.getMessage());
            throw new PaymentsException("계좌 잔액 조회 API 호출 중 오류 발생" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
