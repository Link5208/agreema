package com.example.demo.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.example.demo.domain.User;
import com.example.demo.domain.response.ResultPaginationDTO;

public interface UserService {
	public User handleCreateUser(User user);

	public void handleDeleteUser(long id);

	public User fetchUserById(long id);

	public boolean isEmailExist(String email);

	public ResultPaginationDTO fetchAllUser(Specification<User> specification, Pageable pageable);

	public User handleGetUserByUsername(String username);

	public void updateUserToken(String token, String email);

	public User getUserByRefreshTokenAndEmail(String token, String email);
}
