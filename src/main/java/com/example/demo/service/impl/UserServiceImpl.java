package com.example.demo.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.domain.User;
import com.example.demo.domain.request.ReqLoginDTO;
import com.example.demo.domain.response.ResLoginDTO;
import com.example.demo.domain.response.ResultPaginationDTO;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import com.example.demo.util.SecurityUtil;
import com.example.demo.util.error.IdInvalidException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public User handleSaveUser(User user) {
		if (!this.userRepository.existsByEmail(user.getEmail())) {
			return this.userRepository.save(user);
		}
		return null;
	}

	// public void handleDeleteUser(long id) {
	// fetchUserById(id);
	// this.userRepository.deleteById(id);
	// }

	public void handleDeleteUser(long id) {
		User fetchUser = fetchUserById(id);
		fetchUser.setDeleted(true);
		handleSaveUser(fetchUser);
	}

	public User fetchUserById(long id) throws UsernameNotFoundException {
		Optional<User> userOptional = this.userRepository.findById(id);
		if (userOptional.isPresent()) {
			return userOptional.get();
		} else {
			throw new UsernameNotFoundException("There is not any user having ID = " + id);
		}
	}

	public boolean isEmailExist(String email) {
		return this.userRepository.existsByEmail(email);
	}

	public ResultPaginationDTO fetchAllUser(Specification<User> specification, Pageable pageable) {
		Page<User> page = this.userRepository.findAll(specification, pageable);
		ResultPaginationDTO result = new ResultPaginationDTO();
		ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

		meta.setPage(pageable.getPageNumber() + 1);
		meta.setPageSize(pageable.getPageSize());
		meta.setPages(page.getTotalPages());
		meta.setTotal(page.getTotalElements());

		result.setMeta(meta);
		List<User> list = page.getContent().stream().filter(user -> !user.isDeleted()).toList();

		result.setResult(list);

		return result;
	}

	public User handleGetUserByUsername(String username) {
		return this.userRepository.findByEmail(username);
	}

	public void updateUserToken(String token, String email) {
		User currentUser = this.handleGetUserByUsername(email);
		if (currentUser != null) {
			currentUser.setRefreshToken(token);
			this.userRepository.save(currentUser);
		}
	}

	public User getUserByRefreshTokenAndEmail(String token, String email) {
		return this.userRepository.findByRefreshTokenAndEmail(token, email);
	}

	public String handleCreateUser(User postmanUser) throws IdInvalidException {
		boolean isEmailExist = isEmailExist(postmanUser.getEmail());
		if (isEmailExist) {
			throw new IdInvalidException("Email " + postmanUser.getEmail() + " existed!!!");
		}

		String hashPassword = this.passwordEncoder.encode(postmanUser.getPassword());
		postmanUser.setPassword(hashPassword);
		postmanUser.setDeleted(false);

		User newUser = handleSaveUser(postmanUser);
		return newUser.getEmail();
	}

	public String handleUpdateUser(User postmanUser) {
		User fetchUser = fetchUserById(postmanUser.getId());
		fetchUser.setPassword(postmanUser.getPassword());
		handleSaveUser(fetchUser);
		return fetchUser.getEmail();
	}

	public ResLoginDTO.UserGetAccount handleGetAccount() {
		String email = SecurityUtil.getCurrentUserLogin().isPresent()
				? SecurityUtil.getCurrentUserLogin().get()
				: "";

		User currentUserDB = handleGetUserByUsername(email);

		ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin();
		ResLoginDTO.UserGetAccount userGetAccount = new ResLoginDTO.UserGetAccount();
		if (currentUserDB != null) {
			userLogin.setId(currentUserDB.getId());
			userLogin.setEmail(currentUserDB.getEmail());

			userGetAccount.setUser(userLogin);
		}
		return userGetAccount;
	}
}
