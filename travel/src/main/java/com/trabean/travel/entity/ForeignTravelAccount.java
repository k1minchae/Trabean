package com.trabean.travel.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "foreign_travel_accounts")
public class ForeignTravelAccount {

    @Id
    private Long accountId;

    @Column(nullable = false, length = 5)
    private String exchangeCurrency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_account_id", nullable = false)
    private KrwTravelAccount parentAccount;

    @Builder
    public ForeignTravelAccount(Long accountId, String exchangeCurrency, KrwTravelAccount parentAccount) {
        this.accountId = accountId;
        this.exchangeCurrency = exchangeCurrency;
        this.parentAccount = parentAccount;
    }

}
