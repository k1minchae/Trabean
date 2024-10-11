package com.trabean.travel.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Table(name = "krw_travel_accounts")
public class KrwTravelAccount {

    @Id
    private Long accountId;

    @Setter
    @Column(nullable = false, length = 20)
    private String accountName;

    @Setter
    @Column(nullable = false)
    private Long targetAmount;

    @OneToMany(mappedBy = "parentAccount", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ForeignTravelAccount> childAccounts;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Invitation> invitationList;

}
