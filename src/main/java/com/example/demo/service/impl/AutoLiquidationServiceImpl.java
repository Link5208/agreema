package com.example.demo.service.impl;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.demo.service.ContractService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@Slf4j
public class AutoLiquidationServiceImpl {

	private final ContractService contractService;

	/**
	 * @param contractService
	 */
	public AutoLiquidationServiceImpl(ContractService contractService) {
		this.contractService = contractService;
	}

	@Value("${contract.auto-liquidation.days:365}")
	private long daysUntilLiquidation;

	@Scheduled(cron = "${contract.auto-liquidation.cron}")
	public void autoLiquidateContracts() {
		log.info("Starting auto liquidation check...");

		try {
			Instant liquidationDate = Instant.now().minus(daysUntilLiquidation, ChronoUnit.DAYS);

			contractService.handleAutoLiquidation(liquidationDate);

			log.info("Auto liquidation completed successfully");
		} catch (Exception e) {
			log.error("Error during auto liquidation: ", e);
		}
	}
}
