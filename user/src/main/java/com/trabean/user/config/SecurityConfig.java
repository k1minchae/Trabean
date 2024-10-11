package com.trabean.user.config;

import com.trabean.user.config.jwt.TokenProvider;
import com.trabean.user.config.CustomAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@Slf4j
public class SecurityConfig {

	private final TokenProvider tokenProvider;
	private final UserDetailsService userService;

	// 특정 HTTP 요청에 대한 웹 기반 보안 구성
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		// CustomAuthenticationFilter를 설정하고 필터 체인에 추가
		CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManager(http), tokenProvider);
		customAuthenticationFilter.setFilterProcessesUrl("/api/user/login"); // 로그인 처리 URL 설정

		return http
				.csrf(AbstractHttpConfigurer::disable) // CSRF 보호 비활성화 (API 사용 시 필요에 따라 활성화)
				.authorizeHttpRequests(auth -> auth
						.requestMatchers(
								"/api/user/signup", // 회원가입 URL 허용
								"/api/user/login", // 로그인 URL 허용
								"/api/token").permitAll() // 토큰 발급 URL 허용
						.anyRequest().authenticated()) // 나머지 모든 요청은 인증 필요
				.addFilterBefore(new TokenAuthenticationFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class) // 기존 토큰 인증 필터 추가
				.addFilter(customAuthenticationFilter) // 커스텀 인증 필터 추가
				.build();
	}

	// 인증 관리자 관련 설정하기
	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userService); // 사용자 정보 서비스 설정
		authProvider.setPasswordEncoder(bCryptPasswordEncoder()); // 패스워드 인코더 설정
		return new ProviderManager(authProvider);
	}

	// 패스워드 인코더로 사용할 빈 등록하기
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
