package com.trabean.payment.Entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.trabean.payment.entity.Merchants;
import com.trabean.payment.entity.Payments;
import com.trabean.payment.enums.PaymentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PaymentsEntityTest {

    private Payments payment;

    @BeforeEach
    void setUp() {
        // 가맹점 객체 Mock 생성
        Merchants merchant = mock(Merchants.class);
        when(merchant.getMerchantId()).thenReturn(1L);
        when(merchant.getName()).thenReturn("Test Merchant");

        Long krwAmount = 10000L; // 결제 금액 설정
        Double foreignAmount = 1000.0; // 외화 결제 금액 설정

        // QR 인식 후 결제 생성 메서드 호출
        payment = Payments.createInitialPayment(1L, 100L, merchant, krwAmount, foreignAmount);
    }

    @Test
    @DisplayName("상태변경: SUCCESS로")
    void updatePaymentStatusToSuccess() {
        // Given: 초기 상태는 PENDING
        assertEquals(PaymentStatus.PENDING, payment.getPaymentStatus());

        // When: 결제 상태를 SUCCESS로 변경
        payment.updatePaymentStatus(PaymentStatus.SUCCESS);

        // Then: 상태가 SUCCESS로 변경되었는지 확인
        assertEquals(PaymentStatus.SUCCESS, payment.getPaymentStatus());
    }

    @Test
    @DisplayName("상태변경: CANCEL로")
    void updatePaymentStatusToCancel() {
        // Given: 초기 상태는 PENDING
        assertEquals(PaymentStatus.PENDING, payment.getPaymentStatus());

        // When: 결제 상태를 CANCEL로 변경
        payment.updatePaymentStatus(PaymentStatus.CANCEL);

        // Then: 상태가 CANCEL로 변경되었는지 확인
        assertEquals(PaymentStatus.CANCEL, payment.getPaymentStatus());
    }

    @Test
    @DisplayName("상태변경: PASSWORD_ERROR")
    void updatePaymentStatusToPasswordError() {
        // Given: 초기 상태는 PENDING
        assertEquals(PaymentStatus.PENDING, payment.getPaymentStatus());

        // When: 결제 상태를 PASSWORD_ERROR로 변경
        payment.updatePaymentStatus(PaymentStatus.PASSWORD_ERROR);

        // Then: 상태가 PASSWORD_ERROR로 변경되었는지 확인
        assertEquals(PaymentStatus.PASSWORD_ERROR, payment.getPaymentStatus());
    }
}
