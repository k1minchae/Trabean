package com.trabean.payment.entity;

import com.trabean.payment.enums.PayoutStatus;
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
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payout")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long payoutId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "merchant_id")
    private Merchants merchant;  // Merchant와 외래 키 관계

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PayoutStatus status;

    @Builder
    public Payout(Merchants merchant, PayoutStatus status) {
        this.merchant = merchant;
        this.status = status;
    }

    public void updateStatus(PayoutStatus newStatus) {
        this.status = newStatus; //     SUCCESS, FAILED, PENDING
    }
}