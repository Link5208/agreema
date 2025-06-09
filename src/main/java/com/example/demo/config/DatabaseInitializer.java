package com.example.demo.config;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
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
			String dateString = "2024-01-10";
			String dateString2 = "2025-01-10";
			LocalDate localDate = LocalDate.parse(dateString, DateTimeFormatter.ISO_DATE);
			LocalDate localDate2 = LocalDate.parse(dateString2, DateTimeFormatter.ISO_DATE);
			// Chuyển LocalDate thành Instant ở UTC
			Instant instant = localDate.atStartOfDay().toInstant(ZoneOffset.UTC);
			Instant instant2 = localDate2.atStartOfDay().toInstant(ZoneOffset.UTC);
			Contract contract = new Contract("HD001", "Hợp đồng cung cấp thiết bị văn phòng", instant, instant2,
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
