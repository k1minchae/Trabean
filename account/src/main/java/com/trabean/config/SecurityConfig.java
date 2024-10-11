package com.trabean.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // SecurityFilterChain 빈 등록 (보안 설정)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // CSRF 보호 비활성화 (필요 시)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()  // 모든 요청을 인증 없이 허용
                );
        return http.build();
    }

    // PasswordEncoder 빈 등록 (비밀번호 해싱용)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // BCrypt를 사용한 비밀번호 해싱
    }
}
