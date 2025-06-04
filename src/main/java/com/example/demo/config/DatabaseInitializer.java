package com.example.demo.config;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.domain.Contract;
import com.example.demo.domain.Item;
import com.example.demo.domain.User;
import com.example.demo.repository.ContractRepository;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.constant.EnumStatus;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DatabaseInitializer implements CommandLineRunner {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final ContractRepository contractRepository;
	private final ItemRepository itemRepository;

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		System.out.println(">>>> INIT DATABASE:");
		long countUsers = this.userRepository.count();
		Long countContracts = this.contractRepository.count();

		if (countUsers == 0) {
			User adminUser = new User();
			adminUser.setEmail("admin@gmail.com");
			adminUser.setPassword(this.passwordEncoder.encode("123456"));
			this.userRepository.save(adminUser);
		}

		if (countContracts == 0) {
			String dateTimeString = "2024-01-10 09:00:00";
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			LocalDateTime localDateTime = LocalDateTime.parse(dateTimeString, formatter);
			Instant instant = localDateTime.atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toInstant();
			Contract contract = new Contract("HD001", "Hợp đồng cung cấp thiết bị văn phòng", instant,
					EnumStatus.UNLIQUIDATED, false);
			contract = this.contractRepository.save(contract);

			Item item = new Item("VT001", "Giấy A4", "Ream", 100, 50.00, 5000, false, contract);
			item = this.itemRepository.save(item);
		}

		if (countUsers > 0 && countContracts > 0) {
			System.out.println(">>> SKIP INIT DATABASE");
		} else {
			System.out.println(">>> END INIT DATABASE");
		}
	}

}
