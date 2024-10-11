package com.trabean.user.user.service;

import java.time.Duration;

import org.springframework.stereotype.Service;

import com.trabean.user.config.jwt.TokenProvider;
import com.trabean.user.user.entity.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TokenService {

	private final TokenProvider tokenProvider;
	private final RefreshTokenService refreshTokenService;
	private final UserService userService;

	public String createNewAccessToken(String refreshToken) {
		//토큰 유효성 검사 실패 -> 예외 발생
		if (!tokenProvider.validToken(refreshToken)) {
			throw new IllegalArgumentException("Invalid refresh token");
		}
		String email = refreshTokenService.findByRefreshToken(refreshToken).getEmail();
		User user = userService.findByEmail(email);

		return tokenProvider.generateToken(user, Duration.ofHours(2));
	}
}
