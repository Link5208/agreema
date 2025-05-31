package com.example.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
	Boolean existsByEmail(String email);

	User findByEmail(String email);

	User findByRefreshTokenAndEmail(String refreshToken, String email);

	// @Query("select u from User u where u.deleted = 0")
	Page<User> findAll(Specification<User> specification, Pageable pageable);

}
