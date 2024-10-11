package com.trabean.user.user.service;

import org.springframework.stereotype.Service;

import com.trabean.user.user.entity.RefreshToken;
import com.trabean.user.user.repository.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {

	private final RefreshTokenRepository refreshTokenRepository;

	public RefreshToken findByRefreshToken(String refreshToken) {
		return refreshTokenRepository.findByRefreshToken(refreshToken)
			.orElseThrow(() -> new IllegalArgumentException("Unexpected Token"));
	}
}
