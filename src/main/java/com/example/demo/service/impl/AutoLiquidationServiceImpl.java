package com.example.demo.service.impl;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.demo.service.ContractService;

import lombok.extern.slf4j.Slf4j;

import java.time.Instant;

@Service
@Slf4j
public class AutoLiquidationServiceImpl {

	private final ContractService contractService;

	public AutoLiquidationServiceImpl(ContractService contractService) {
		this.contractService = contractService;
	}

	@Scheduled(cron = "${contract.auto-liquidation.cron}")
	public void autoLiquidateContracts() {
		log.info("Starting auto liquidation check...");

		try {
			// Get current time
			Instant now = Instant.now();

			// Auto liquidate contracts where liquidationDate has passed
			contractService.handleAutoLiquidation(now);

			log.info("Auto liquidation completed successfully");
		} catch (Exception e) {
			log.error("Error during auto liquidation: ", e);
		}
	}
}
