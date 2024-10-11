package com.trabean.account.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "userAccounts")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserAccountRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userAccountId")
    private Long userAccountRelationId;

    @Column(nullable = false)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountId", nullable = false)
    private Account account;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole userRole;

    public enum UserRole {
        ADMIN,
        PAYER,
        NONE_PAYER
    }
}
