package com.trabean.user.config.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Collections;
import java.util.Set;

import com.trabean.user.user.entity.User;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
@Slf4j
public class TokenProvider {

	private final JwtProperties jwtProperties;

	// SecretKey를 생성하여 Key 객체로 변환
	private Key getSigningKey() {
		byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecretKey());
		return Keys.hmacShaKeyFor(keyBytes);
	}

	public String generateToken(User user, java.time.Duration expiredAt) {
		Date now = new Date();
		return makeToken(new Date(now.getTime() + expiredAt.toMillis()), user);
	}

	private String makeToken(Date expiredAt, User user) {
		Date now = new Date();

		return Jwts.builder()
				.setHeaderParam(Header.TYPE, Header.JWT_TYPE)
				.setIssuer(jwtProperties.getIssuer())
				.setIssuedAt(now)
				.setExpiration(expiredAt)
				.setSubject(user.getEmail())
				.claim("email", user.getEmail())
				.signWith(getSigningKey(), SignatureAlgorithm.HS256) // SecretKey 객체를 사용하여 서명
				.compact();
	}

	public boolean validToken(String token) {
		try {
			Jwts.parserBuilder()
					.setSigningKey(getSigningKey()) // SecretKey 객체 사용
					.build()
					.parseClaimsJws(token);
			return true;
		} catch (SecurityException | MalformedJwtException e) {
			log.info("잘못된 JWT 서명입니다.");
		} catch (ExpiredJwtException e) {
			log.info("만료된 JWT 토큰입니다.");
		} catch (UnsupportedJwtException e) {
			log.info("지원되지 않는 JWT 토큰입니다.");
		} catch (IllegalArgumentException e) {
			log.info("JWT 토큰이 잘못되었습니다.");
		}
		return false;
	}

	public Authentication getAuthentication(String token) {
		Claims claims = getClaims(token);
		Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
		return new UsernamePasswordAuthenticationToken(new org.springframework.security.core.userdetails.User(claims.getSubject(), "", authorities), token, authorities);
	}

	public Long getUserId(String token) {
		Claims claims = getClaims(token);
		return claims.get("id", Long.class);
	}

	public Claims getClaims(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(getSigningKey()) // SecretKey 객체 사용
				.build()
				.parseClaimsJws(token)
				.getBody();
	}
}
