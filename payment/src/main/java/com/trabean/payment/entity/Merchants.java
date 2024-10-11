package com.trabean.payment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Merchants {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long merchantId;

    @Column(length = 16, nullable = false)
    private String accountNo;

    @Column(length = 20, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MerchantCategory category;

    @Column(length = 5, nullable = false)
    private String exchangeCurrency;

    @Builder
    public Merchants(String accountNo, String name, MerchantCategory category, String exchangeCurrency) {
        this.accountNo = accountNo;
        this.name = name;
        this.category = category;
        this.exchangeCurrency = exchangeCurrency;
    }
}