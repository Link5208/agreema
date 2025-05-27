package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.demo.domain.User;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
	Boolean existsByEmail(String email);

	User findByEmail(String email);

	User findByRefreshTokenAndEmail(String refreshToken, String email);

}
