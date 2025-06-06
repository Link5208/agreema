package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.User;
import com.example.demo.domain.request.ReqLoginDTO;
import com.example.demo.domain.response.ResLoginDTO;
import com.example.demo.service.UserService;
import com.example.demo.util.SecurityUtil;
import com.example.demo.util.annotation.ApiMessage;
import com.example.demo.util.error.IdInvalidException;

import jakarta.validation.Valid;

@RestController

@RequestMapping("/api/v1")
public class AuthController {
	private final AuthenticationManagerBuilder authenticationManagerBuilder;
	private final SecurityUtil securityUtil;
	private final UserService userService;

	/**
	 * @param authenticationManagerBuilder
	 * @param securityUtil
	 * @param userService
	 */
	public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, SecurityUtil securityUtil,
			UserService userService, PasswordEncoder passwordEncoder) {
		this.authenticationManagerBuilder = authenticationManagerBuilder;
		this.securityUtil = securityUtil;
		this.userService = userService;
	}

	@Value("${hoanglong.jwt.refresh-token-validity-in-seconds}")
	private long refreshTokenExpiration;

	@PostMapping("/auth/register")
	@ApiMessage("Register a new user")
	public ResponseEntity<User> register(@Valid @RequestBody User postmanUser) throws IdInvalidException {

		return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.handleCreateUser(postmanUser));
	}

	@PostMapping("/auth/login")
	public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody ReqLoginDTO loginDTO) {

		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
				loginDTO.getUsername(), loginDTO.getPassword());

		Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		ResLoginDTO res = new ResLoginDTO();
		User currentUserDB = this.userService.handleGetUserByUsername(loginDTO.getUsername());
		if (currentUserDB != null) {

			ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(
					currentUserDB.getId(),
					currentUserDB.getEmail());

			res.setUser(userLogin);
		}

		String access_token = this.securityUtil.createAccessToken(authentication.getName(), res);
		res.setAccessToken(access_token);
		String refresh_token = this.securityUtil.createRefreshToken(loginDTO.getUsername(), res);

		this.userService.updateUserToken(refresh_token, loginDTO.getUsername());
		ResponseCookie responseCookie = ResponseCookie
				.from("refresh_token", refresh_token)
				.httpOnly(true)
				.secure(true)
				.path("/")
				.maxAge(refreshTokenExpiration)
				.build();

		return ResponseEntity.ok()
				.header(HttpHeaders.SET_COOKIE, responseCookie.toString())
				.body(res);

	}

	@GetMapping("/auth/account")
	@ApiMessage("Fetch account")
	public ResponseEntity<ResLoginDTO.UserGetAccount> getAccount() {

		return ResponseEntity.ok().body(this.userService.handleGetAccount());
	}

	@GetMapping("/auth/refresh")
	@ApiMessage("Get user by refresh token")
	public ResponseEntity<ResLoginDTO> getRefreshToken(
			@CookieValue(name = "refresh_token", defaultValue = "abc") String refresh_token) throws IdInvalidException {

		if (refresh_token.equals("abc")) {
			throw new IdInvalidException("You don't have Refresh Token of Cookie'");
		}

		Jwt decodedToken = this.securityUtil.checkValidRefreshToken(refresh_token);
		String email = decodedToken.getSubject();

		User currentUser = this.userService.getUserByRefreshTokenAndEmail(refresh_token, email);
		if (currentUser == null) {
			throw new IdInvalidException("Refresh Token is invalid");
		}
		ResLoginDTO res = new ResLoginDTO();
		User currentUserDB = this.userService.handleGetUserByUsername(email);
		if (currentUserDB != null) {

			ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(
					currentUserDB.getId(),
					currentUserDB.getEmail());

			res.setUser(userLogin);
		}

		String access_token = this.securityUtil.createAccessToken(email, res);
		res.setAccessToken(access_token);

		String new_refresh_token = this.securityUtil.createRefreshToken(email, res);

		this.userService.updateUserToken(new_refresh_token, email);
		ResponseCookie responseCookie = ResponseCookie
				.from("refresh_token", new_refresh_token)
				.httpOnly(true)
				.secure(true)
				.path("/")
				.maxAge(refreshTokenExpiration)
				.build();

		return ResponseEntity.ok()
				.header(HttpHeaders.SET_COOKIE, responseCookie.toString())
				.body(res);

	}

	@PostMapping("/auth/logout")
	@ApiMessage("Logout User")
	public ResponseEntity<Void> Logout() throws IdInvalidException {
		String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
		if (email.equals("")) {
			throw new IdInvalidException("Access Token is invalid.");
		}
		this.userService.updateUserToken(null, email);
		ResponseCookie deleteCookie = ResponseCookie
				.from("refresh_token", null)
				.httpOnly(true)
				.secure(true)
				.path("/")
				.maxAge(0)
				.build();
		return ResponseEntity
				.ok()
				.header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
				.body(null);
	}

}
