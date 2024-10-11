package com.trabean.travel.repository;

import com.trabean.travel.entity.Invitation;
import com.trabean.travel.entity.KrwTravelAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {

    Invitation findByEmailAndAccount(String email, KrwTravelAccount account);

}
