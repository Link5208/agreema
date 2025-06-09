package com.example.demo.service.impl;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.demo.domain.Contract;
import com.example.demo.service.ContractService;
import com.example.demo.service.EmailService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class AutoLiquidationServiceImpl {

	private final ContractService contractService;
	private final EmailService emailService;

	@Scheduled(cron = "${contract.auto-liquidation.cron}")
	public void autoLiquidateContracts() {
		log.info("Starting auto liquidation check...");

		try {
			// Get current time
			Instant now = Instant.now();
			List<Contract> liquidatedContracts = contractService.handleAutoLiquidation(now);

			// Send notification for liquidated contracts
			for (Contract contract : liquidatedContracts) {
				emailService.sendContractNotification(
						contract,
						"Contract Automatically Liquidated",
						"Your contract has been automatically liquidated.");
			}

			log.info("Auto liquidation completed successfully");
		} catch (Exception e) {
			log.error("Error during auto liquidation: ", e);
		}
	}

	@Scheduled(cron = "${contract.notification.cron}")
	public void checkAndNotify() {
		log.info("Starting notification check...");
		Instant now = Instant.now();

		try {
			// Check contracts 3 days before liquidation
			Instant threeDaysFromNow = now.plus(3, ChronoUnit.DAYS);
			List<Contract> contractsIn3Days = contractService.findContractsToLiquidateAt(threeDaysFromNow);

			for (Contract contract : contractsIn3Days) {
				emailService.sendContractNotification(
						contract,
						"Contract Liquidation Reminder - 3 Days",
						"Your contract will be automatically liquidated in 3 days.");
			}

			// Check contracts 1 day before liquidation
			Instant oneDayFromNow = now.plus(1, ChronoUnit.DAYS);
			List<Contract> contractsIn1Day = contractService.findContractsToLiquidateAt(oneDayFromNow);

			for (Contract contract : contractsIn1Day) {
				emailService.sendContractNotification(
						contract,
						"Contract Liquidation Reminder - 1 Day",
						"Your contract will be automatically liquidated tomorrow.");
			}
		} catch (Exception e) {
			log.error("Error during notification check: ", e);
		}
	}
}
