package com.trabean.payment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payments {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long payId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long accountId;  // Account는 MSA 구조로 다른 프로젝트에 있으므로 accountId만 저장

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id")
    private Merchants merchant;  // Merchant 엔티티와 외래 키 관계

    @Column(unique = true, nullable = false, length = 100)
    private String transactionId;

    @Column
    private Timestamp paymentDate;

    @Column
    private Long krwAmount;

    @Column
    private Double foreignAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus;

    @Column(nullable = false)
    private Integer passwordErrorCount = 0;

    @Builder
    // 결제 초기 생성 메서드 (최초 저장 시)
    public static Payments createInitialPayment(Long userId, Long accountId) {
        Payments payment = new Payments();
        payment.userId = userId;
        payment.accountId = accountId;
        payment.transactionId = UUID.randomUUID().toString();  // 트랜잭션 ID 생성
        payment.paymentStatus = PaymentStatus.PENDING;
        payment.paymentDate = new Timestamp(System.currentTimeMillis());  // 현재 시간 저장
        return payment;
    }

    // 추가 결제 정보 업데이트 메서드 (추가 결제 요청 시)
    public void updatePaymentDetails(Long krwAmount, Double foreignAmount, Merchants merchant) {
        // 성공 시
        this.krwAmount = krwAmount;
        this.foreignAmount = foreignAmount;
        this.merchant = merchant;
        this.paymentStatus = PaymentStatus.SUCCESS;
        this.paymentDate = new Timestamp(System.currentTimeMillis()); // 시간 업데이트
    }
}