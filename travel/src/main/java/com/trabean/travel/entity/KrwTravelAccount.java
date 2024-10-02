package com.trabean.travel.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "krw_travel_accounts")
public class KrwTravelAccount {

    @Id
    private Long accountId;

    @Column(nullable = false, length = 20)
    private String accountName;

    @Column(nullable = false)
    private Long targetAmount;

    @OneToMany(mappedBy = "parentAccount", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ForeignTravelAccount> childAccounts;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Invitation> invitationList;

    @Builder
    public KrwTravelAccount(Long accountId, String accountName, Long targetAmount) {
        this.accountId = accountId;
        this.accountName = accountName;
        this.targetAmount = targetAmount;
    }

    public void changeAccountName(String newAccountName) {
        if (newAccountName == null) {
            throw new IllegalArgumentException("계좌 이름은 null일 수 없습니다.");
        } else if (newAccountName.length() > 20) {
            throw new IllegalArgumentException("계좌 이름은 20자 이내여야 합니다.");
        }

        this.accountName = newAccountName;
    }

    public void changeTargetAmount(Long newTargetAmount) {
        if (newTargetAmount == null) {
            throw new IllegalArgumentException("목표 금액은 null일 수 없습니다.");
        } else if (newTargetAmount < 0) {
            throw new IllegalArgumentException("목표 금액은 음수일 수 없습니다.");
        }

        this.targetAmount = newTargetAmount;
    }

}
