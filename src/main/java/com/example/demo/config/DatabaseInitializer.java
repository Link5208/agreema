package com.example.demo.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.domain.User;
import com.example.demo.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DatabaseInitializer implements CommandLineRunner {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		System.out.println(">>>> INIT DATABASE:");
		long countUsers = this.userRepository.count();

		if (countUsers == 0) {
			User adminUser = new User();
			adminUser.setEmail("admin@gmail.com");
			adminUser.setPassword(this.passwordEncoder.encode("123456"));
			this.userRepository.save(adminUser);
		}

		if (countUsers > 0) {
			System.out.println(">>> SKIP INIT DATABASE");
		} else {
			System.out.println(">>> END INIT DATABASE");
		}
	}

}
