package com.example.demo.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.domain.User;
import com.example.demo.domain.response.ResultPaginationDTO;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	public User handleCreateUser(User user) {
		if (!this.userRepository.existsByEmail(user.getEmail())) {
			return this.userRepository.save(user);
		}
		return null;
	}

	public void handleDeleteUser(long id) {
		fetchUserById(id);
		this.userRepository.deleteById(id);
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
}
