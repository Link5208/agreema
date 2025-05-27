package com.example.demo.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.User;
import com.example.demo.domain.response.ResultPaginationDTO;
import com.example.demo.service.UserService;
import com.example.demo.util.annotation.ApiMessage;
import com.example.demo.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class UserController {
	private final UserService userService;
	private final PasswordEncoder passwordEncoder;

	/**
	 * @param userService
	 * @param passwordEncoder
	 */
	public UserController(UserService userService, PasswordEncoder passwordEncoder) {
		this.userService = userService;
		this.passwordEncoder = passwordEncoder;
	}

	@PostMapping("/users")
	@ApiMessage("Create a new user")
	public ResponseEntity<String> createNewUser(@Valid @RequestBody User postManUser)
			throws IdInvalidException {

		boolean isEmailExist = this.userService.isEmailExist(postManUser.getEmail());
		if (isEmailExist) {
			throw new IdInvalidException("Email " + postManUser.getEmail() + " existed!!!");
		}

		String hashPassword = this.passwordEncoder.encode(postManUser.getPassword());
		postManUser.setPassword(hashPassword);

		User newUser = this.userService.handleCreateUser(postManUser);

		return ResponseEntity.status(HttpStatus.CREATED).body(newUser.getEmail());
	}

	// fetch user by id
	@GetMapping("/users/{id}")
	@ApiMessage("Fetch user by id")
	public ResponseEntity<String> getUserById(@PathVariable("id") long id) throws IdInvalidException {
		User fetchUser = this.userService.fetchUserById(id);

		return ResponseEntity.status(HttpStatus.OK).body(fetchUser.getEmail());
	}

	@DeleteMapping("/users/{id}")
	@ApiMessage("Delete an user")
	public ResponseEntity<String> deleteUser(@PathVariable("id") long id) throws IdInvalidException {
		this.userService.handleDeleteUser(id);

		return ResponseEntity.status(HttpStatus.OK).body("Delete Success");
	}

	// fetch all users
	@GetMapping("/users")
	@ApiMessage("Fetch all users")
	public ResponseEntity<ResultPaginationDTO> getAllUser(
			@Filter Specification<User> specification,
			Pageable pageable) {
		return ResponseEntity.status(HttpStatus.OK).body(
				this.userService.fetchAllUser(specification, pageable));

	}

}
