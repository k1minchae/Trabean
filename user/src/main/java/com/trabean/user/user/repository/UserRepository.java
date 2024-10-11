package com.trabean.user.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trabean.user.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email); //email로 사용자 식별하기
	boolean existsByEmail(String email);//이 이메일을 가진 사람이 있나?
}
