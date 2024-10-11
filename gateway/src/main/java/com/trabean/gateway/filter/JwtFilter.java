package com.trabean.gateway.filter;

import com.trabean.gateway.client.feign.UserFeign;
import com.trabean.gateway.util.Encryption;
import com.trabean.gateway.util.JwtManger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 글로벌 filter로 적용할지 특정 라우터에 적용할지는 yml 파일에서 설정
 */
@Component
public class JwtFilter extends AbstractGatewayFilterFactory<JwtFilter.Config> {

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class); // 로거 인스턴스 생성
    private final JwtManger jwtManger; // JWT 유효성 검사를 위한
    private final UserFeign userFeign;

    // 생성자에서 JwtManger와 UserFeign을 주입받음
    // @Lazy 사용하면 실제로 userFeign이 사용될때 주입
    @Autowired
    public JwtFilter(JwtManger jwtManger, @Lazy UserFeign userFeign) {
        super(Config.class); // 설정 클래스를 지정
        this.jwtManger = jwtManger; // 주입받은 jwtManger를 필드에 저장
        this.userFeign = userFeign;
    }

    /**
     * 필터의 주요 로직을 정의하는 메서드
     */
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            logger.info("@@@@@@@@@@jwt filter!"); // 필터가 실행됨을 알림

            HttpHeaders headers = exchange.getRequest().getHeaders(); // 요청의 HTTP 헤더를 가져옴
            String accessToken = "";
            try {
                accessToken = Objects.requireNonNull(headers.getFirst("Authorization")).substring(7); // 'Authorization' 헤더에서 토큰 값을 가져옴
            } catch (NullPointerException e) {
                logger.info("accessToken is null!"); // accessToken이 없음을 로그에 기록
                exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST); // 400 Bad Request 응답 설정
                return exchange.getResponse().setComplete(); // 응답을 완료 상태로 설정
            }

            // accessToken의 유효성을 검사
            if (!jwtManger.checkTokenValidation(accessToken)) {
                logger.warn("accessToken is not valid."); // 유효하지 않은 accessToken 로그 출력
                exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST); // 400 Bad Request 응답 설정
                return exchange.getResponse().setComplete(); // 응답을 완료 상태로 설정

            }

            logger.info("accessToken has been validated."); // 유효한 토큰 로그 출력

            // JWT에서 추출
            Long userId = jwtManger.getUserId(accessToken);
            String userKey = jwtManger.getUserKey(accessToken);


            try {
                // userId와 userKey를 암호화
                String encryptedUserId = Encryption.encrypt(String.valueOf(userId));
                String encryptedUserKey = Encryption.encrypt(userKey);

//             헤더에 암호화된 userId와 userKey 추가
                exchange.getRequest().mutate()
                        .header("userId", encryptedUserId)
                        .header("userKey", encryptedUserKey)
                        .build();
//                exchange.getRequest().mutate()
//                        .header("userId", userId.toString())
//                        .header("userKey", userKey)
//                        .build();
            } catch (Exception e) {
                logger.error("Encryption error: ", e);
                exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
                return exchange.getResponse().setComplete();
            }

            // 요청 정보를 JSON 형태로 로그에 출력
            logger.info("gateway에서 요청 전달: method={}, uri={}, headers={}",
                    exchange.getRequest().getMethod(),
                    exchange.getRequest().getURI(),
                    exchange.getRequest().getHeaders());
            return chain.filter(exchange); // 다음 필터로 요청을 전달
        };
    }

    // 필터 설정을 위한 내부 클래스
    public static class Config {
        // 필요한 경우 설정 속성을 여기에 추가
    }
}
