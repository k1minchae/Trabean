package com.trabean.user.user.entity;

import java.util.Collection;
import java.util.List;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class User implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id", updatable = false, nullable = false)
	private Long user_id;

	@Column(name="user_key",nullable = false)
	private String user_key;

	@Column(name = "email", nullable = false, unique = true)
	private String email; // 사용자 이메일

	@Column(name = "password", nullable = false)
	private String password; // 사용자 비밀번호

	@Column(name = "name", nullable = false)
	private String name; // 사용자 성명

	@Column(name = "payment_account_id")
	private String payment_account_id;//사용자 계좌

	@Column(name = "main_account_id")
	private String main_account_id;//주 사용계좌

	@Enumerated(EnumType.STRING)
	private Authority authority;

	@Builder
	public User(String name, String email, String password, String user_key) {
		this.name = name;
		this.email = email;
		this.password = password;
		 this.user_key = user_key;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// 권한 반환하는 로직
		return List.of(new SimpleGrantedAuthority("user"));
	}

	@Override
	public String getUsername() {
		// name 필드를 username으로 사용
		return name;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true; // 패스워드가 만료되지 않았음을 반환
	}

	@Override
	public boolean isEnabled() {
		return true; // 계정이 활성화되었음을 반환
	}

	@Override
	public boolean isAccountNonLocked() {
		return true; // 계정이 잠기지 않았음을 반환
	}

	@Override
	public boolean isAccountNonExpired() {
		return true; // 계정이 만료되지 않았음을 반환
	}
}
