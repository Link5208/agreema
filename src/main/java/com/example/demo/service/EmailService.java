package com.example.demo.service;

import com.example.demo.domain.Contract;

public interface EmailService {
	public void sendContractNotification(Contract contract, String subject, String message);

}
