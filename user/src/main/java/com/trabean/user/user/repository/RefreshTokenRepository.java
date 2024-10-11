package com.trabean.user.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trabean.user.user.entity.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

	Optional<RefreshToken> findByEmail(String email);
	Optional<RefreshToken> findByRefreshToken(String refreshToken);

}
