package com.trabean.travel.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "invitations")
public class Invitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long invitationId;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false)
    private boolean isAccepted;

    @Column(nullable = false)
    private Timestamp inviteDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private KrwTravelAccount account;

    @Builder
    public Invitation(String email, boolean isAccepted, Timestamp inviteDate, KrwTravelAccount account) {
        this.email = email;
        this.isAccepted = isAccepted;
        this.inviteDate = inviteDate;
        this.account = account;
    }

    public void changeIsAccepted(boolean isAccepted) {
        this.isAccepted = isAccepted;
    }

}
