package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.service.EmailService;
import com.example.demo.service.ContractService;
import lombok.AllArgsConstructor;
import com.example.demo.domain.Contract;

@RestController
@RequestMapping("/api/v1/email")
@AllArgsConstructor
public class EmailController {

	private final EmailService emailService;
	private final ContractService contractService;

	@PostMapping("/test/{id}")
	public ResponseEntity<String> testEmail(@PathVariable("id") long id) {
		try {
			Contract contract = contractService.findContractById(id);

			// Test 3-day notification
			emailService.sendContractNotification(
					contract,
					"Test: Contract Liquidation Reminder - 3 Days",
					"TEST EMAIL: Your contract will be automatically liquidated in 3 days.");

			// Test 1-day notification
			emailService.sendContractNotification(
					contract,
					"Test: Contract Liquidation Reminder - 1 Day",
					"TEST EMAIL: Your contract will be automatically liquidated tomorrow.");

			// Test liquidation notification
			emailService.sendContractNotification(
					contract,
					"Test: Contract Automatically Liquidated",
					"TEST EMAIL: Your contract has been automatically liquidated.");

			return ResponseEntity.ok("Test emails sent successfully");
		} catch (Exception e) {
			return ResponseEntity.internalServerError()
					.body("Failed to send test emails: " + e.getMessage());
		}
	}
}