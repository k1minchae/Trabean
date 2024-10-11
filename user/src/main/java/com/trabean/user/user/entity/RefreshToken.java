package com.trabean.user.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "refresh_token")
public class RefreshToken {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;  // SQL 테이블의 BIGINT 타입 id와 매핑됨

	@Column(nullable = false, length = 100)
	private String email;  // SQL 테이블의 email과 매핑됨

	@Column(name = "refresh_token", nullable = false, length = 255)
	private String refreshToken;  // SQL 테이블의 refresh_token과 매핑됨

	public RefreshToken updateValue(String token) {
		this.refreshToken = token;
		return this;
	}
}
