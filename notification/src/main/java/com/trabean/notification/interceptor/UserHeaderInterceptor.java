package com.trabean.notification.interceptor;

import com.trabean.notification.util.Decryption;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class UserHeaderInterceptor implements HandlerInterceptor {

    public static ThreadLocal<Long> userId = new ThreadLocal<>();
    public static ThreadLocal<String> userKey = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        String userIdHeader = request.getHeader("userId");
        String userKeyHeader = request.getHeader("userKey");

        if (userIdHeader != null) {
            try {
                log.info("userIdHeader: {}", userIdHeader);
                userId.set(Long.parseLong(Decryption.decrypt(userIdHeader)));
                log.info("userId: {}", userId.get());
            } catch (RuntimeException e) {
                throw new IllegalArgumentException();
            }
        }

        if (userKeyHeader != null) {
            log.info("userKeyHeader: {}", userKeyHeader);
            userKey.set(Decryption.decrypt(userKeyHeader));
            log.info("userKey: {}", userKey.get());
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        userId.remove();
        userKey.remove();
    }

}