package com.example.demo.service;

import com.example.demo.domain.User;

public interface UserService {
	public User handleCreateUser(User user);

	public void handleDeleteUser(long id);

	public User fetchUserById(long id);

	public boolean isEmailExist(String email);

	public User handleGetUserByUsername(String username);

	public void updateUserToken(String token, String email);

	public User getUserByRefreshTokenAndEmail(String token, String email);
}
