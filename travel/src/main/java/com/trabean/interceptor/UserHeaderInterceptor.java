package com.trabean.interceptor;

import com.trabean.util.Decryption;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

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
                userId.set(Long.parseLong(Decryption.decrypt(userIdHeader)));
            } catch (RuntimeException e) {
                throw new IllegalArgumentException();
            }
        }

        if (userKeyHeader != null) {
            userKey.set(Decryption.decrypt(userKeyHeader));
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        userId.remove();
        userKey.remove();
    }

}