package com.trabean.travel.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
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

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private KrwTravelAccount account;

}
