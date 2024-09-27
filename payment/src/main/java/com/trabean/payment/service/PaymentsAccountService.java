package com.trabean.payment.service;

import com.trabean.payment.client.AccountClient;
import com.trabean.payment.client.TravelClient;
import com.trabean.payment.client.UserClient;
import com.trabean.payment.client.ssafy.DemandDepositClient;
import com.trabean.payment.dto.request.BalanceRequest;
import com.trabean.payment.dto.request.Header;
import com.trabean.payment.dto.request.RequestPaymentRequest;
import com.trabean.payment.dto.response.AccountNoResponse;
import com.trabean.payment.dto.response.BalanceResponse;
import com.trabean.payment.entity.Merchants;
import com.trabean.payment.entity.Payments;
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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

@Service
@RequiredArgsConstructor
public class PaymentsAccountService {

    private final AccountClient accountClient;
    private final DemandDepositClient demandDepositClient;
    private final TravelClient travelClient;
    private final MerchantsRepository merchantsRepository;
    private static final Logger logger = LoggerFactory.getLogger(PaymentsAccountService.class);
    private final UserClient userClient;
    private final PaymentsRepository paymentsRepository;

    // 유저 키 임시 설정
//    @Value("9e10349e-91e9-474d-afb4-564b24178d9f")
//    private String userKey;

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

    // 한화 잔액 조회 후 검증
    public void validateKrwAmount(Long krwAccountId, RequestPaymentRequest requestPaymentRequest) {
        // 유저 키 받아오기
        String userKey = getAccountAdmin(krwAccountId);

        String accountNo = getAccountNumber(krwAccountId);

        // BalanceRequest 생성
        BalanceRequest balanceRequest = new BalanceRequest(
                Header.builder().apiName(ApiName.KRW_BALANCE).userKey(userKey).build(),
                accountNo
        );

        try {
            BalanceResponse response = demandDepositClient.getKRWBalance(balanceRequest);

            // 결과 로그 남기기
            if (response != null && response.getRec() != null) {
                logger.info("한화 계좌 잔액 조회: {}", response.getRec().getAccountBalance());

                // 잔액 부족
                if (response.getRec().getAccountBalance() < requestPaymentRequest.getKrwAmount()) {
                    logger.info("한화 계좌 잔액 부족");
                    throw new PaymentsException("계좌 잔액이 부족합니다." + response.getRec().getAccountBalance(),
                            HttpStatus.PAYMENT_REQUIRED); // 402
                } else {
                    logger.info("한화 계좌 출금 가능");
                }
            } else {
                logger.warn("한화 계좌 잔액 조회 실패");
                throw new PaymentsException("응답값이 잘못되었습니다. 잔액이 null 값 입니다.",
                        HttpStatus.NOT_FOUND); // 404
            }
        } catch (RestClientException e) {
            logger.error("한화 계좌 잔액 조회 API 호출 중 오류 발생: {}", e.getMessage());
            throw new PaymentsException("한화 계좌 잔액 조회 API 호출 중 오류 발생" + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 외화 계좌 잔액 조회 후 검증
    public void validateForeignAmount(Long foreignAccountId, RequestPaymentRequest requestPaymentRequest) {
        // 메인 통장 주인 userKey 받아오기
        Long krwAccountId = getMainAccount(requestPaymentRequest.getUserId());
        String userKey = getAccountAdmin(krwAccountId);

        String accountNo = getAccountNumber(foreignAccountId);

        // BalanceRequest 생성
        BalanceRequest balanceRequest = new BalanceRequest(
                Header.builder().apiName(ApiName.FOREIGN_BALANCE).userKey(userKey).build(),
                accountNo
        );

        try {
            BalanceResponse response = demandDepositClient.getFORBalance(balanceRequest);

            // 결과 로그 남기기
            if (response != null && response.getRec() != null) {
                logger.info("외화 계좌 잔액 조회: {}", response.getRec().getAccountBalance());

                // 잔액 부족
                if (response.getRec().getAccountBalance() < requestPaymentRequest.getForeignAmount()) {
                    logger.info("외화 계좌 잔액 부족");

                    // 한국 계좌 잔액도 검증해봄
                    validateKrwAmount(krwAccountId, requestPaymentRequest);
    
                    Payments payment = paymentsRepository.findById(requestPaymentRequest.getPayId()).orElseThrow(() ->
                            new PaymentsException("결제 정보를 확인할 수 없습니다.", HttpStatus.NOT_FOUND));

                    throw new PaymentsException(
                            "FOREIGN_ACCOUNT_BALANCE_ERROR", payment.getKrwAmount(),
                            HttpStatus.PAYMENT_REQUIRED); // 402 한화로 결제할 경우 결제 금액 보여줌.
                } else {
                    logger.info("외화 계좌 출금 가능");
                }
            } else {
                logger.warn("외화 계좌 잔액 조회 실패");
                throw new PaymentsException("응답값이 잘못되었습니다. 잔액이 null 값 입니다.",
                        HttpStatus.NOT_FOUND); // 404
            }
        } catch (RestClientException e) {
            logger.error("계좌 잔액 조회 API 호출 중 오류 발생: {}", e.getMessage());
            throw new PaymentsException("계좌 잔액 조회 API 호출 중 오류 발생" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Long getFORAccount(Long merchantId, Long accountId) {
        Merchants merchant = merchantsRepository.findById(merchantId)
                .orElseThrow(() -> new PaymentsException("잘못된 merchantId값 입니다.", HttpStatus.BAD_REQUEST)); // 400

        // 한국 결제일 경우
        if (merchant.getExchangeCurrency().equals("KRW")) {
            return accountId;
        }
        Map<String, Long> response = travelClient.getFORAccount(accountId, merchant.getExchangeCurrency());

        return response.getOrDefault("foreignTravelAccountsId", null);
    }

    // 메인 여행통장 (한화) 불러오기
    public Long getMainAccount(Long userId) {
        Map<String, Long> response = userClient.getPaymentAccount(userId);

        return response.getOrDefault("paymentAccountId", null);
    }

    public String getAccountAdmin(Long accountId) throws PaymentsException {
        String requestBody = String.format("{\"accountId\":\"%s\"}", accountId);
        Map<String, String > response = accountClient.getAdminUser(requestBody);
        if (response.get("userKey") == null) {
            throw new PaymentsException("여행 통장 userKey 를 받아오는 데 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response.get("userKey");
    }
}
