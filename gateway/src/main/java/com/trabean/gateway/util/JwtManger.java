package com.trabean.gateway.util;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component // 이 클래스가 Spring의 컴포넌트로 등록됨을 나타냄
public class JwtManger {
    private static final Logger logger = LoggerFactory.getLogger(JwtManger.class); // 로거 인스턴스 생성

    /**
     * JWT 토큰을 생성하는 과정에서 알고리즘(예: HMAC SHA256)과 key가 결합되어 Signature가 생성됩니다.
     * 때문에 서명 생성 및 검증 과정에서 동일한 key를 사용해야 JWT가 올바르게 처리될 수 있습니다.
     */
    private final String key = "NiOeyFbN1Gqo10bPgUyTFsRMkJpGLXSvGP04eFqj5B30r5TcrtlSXfQ7TndvYjNvfkEKLqILn0j1SmKODO1Yw3JpBBgI3nVPEahqxeY8qbPSFGyzyEVxnl4AQcrnVneI"; // JWT 서명을 위한 비밀 키

    // 변조 검사 및 유효성 검사를 수행하는 메서드
    public boolean checkTokenValidation(String accessToken) {
        try {
            // JWT 파서 빌더를 사용하여 토큰을 검증
            Jwts.parserBuilder()
                    .setSigningKey(key) // 비밀 키를 설정
                    .build() // 파서 빌더 구축
                    .parseClaimsJws(accessToken); // 토큰을 검증하고 클레임을 파싱

            return true; // 토큰이 유효한 경우 true 반환
        } catch (UnsupportedJwtException e) {
            // 지원되지 않는 JWT 토큰일 경우
            logger.warn("Unsupported JWT token"); // 로그 경고 출력
            return false; // 유효하지 않은 토큰
        } catch (MalformedJwtException e) {
            // 형식이 잘못된 JWT 토큰일 경우
            logger.warn("Malformed JWT token"); // 로그 경고 출력
            return false; // 유효하지 않은 토큰
        } catch (SignatureException e) {
            // 서명이 유효하지 않은 JWT 토큰일 경우
            logger.warn("Invalid JWT token"); // 로그 경고 출력
            return false; // 유효하지 않은 토큰
        } catch (ExpiredJwtException e) {
            // 만료된 JWT 토큰일 경우
            logger.warn("Expired JWT token"); // 로그 경고 출력
            return false; // 유효하지 않은 토큰
        }

    }

    public Long getUserId(String accessToken) {

        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody()
                    .get("userId", Long.class);

        } catch (Exception e) {
            logger.error("토큰에서 userId값을 가져오는데에 실패", e);
            return null;
        }
    }

    public String getUserKey(String accessToken) {

        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody()
                    .get("userKey", String.class);

        } catch (Exception e) {
            logger.error("토큰에서 userKey값을 가져오는데에 실패", e);
            return null;
        }
    }
}
