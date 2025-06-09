package com.example.demo.service.impl;

import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.demo.domain.Contract;
import com.example.demo.service.EmailService;
import com.example.demo.util.constant.EnumStatus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {
	private JavaMailSender mailSender;

	@Value("${spring.mail.username}")
	private String fromEmail;

	/**
	 * @param mailSender
	 */
	public EmailServiceImpl(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void sendContractNotification(Contract contract, String subject, String message) {
		try {
			SimpleMailMessage mailMessage = new SimpleMailMessage();
			mailMessage.setFrom(fromEmail);
			mailMessage.setTo(contract.getCreatedBy());
			mailMessage.setSubject(subject);
			mailMessage.setText(createEmailContent(contract, message));

			mailSender.send(mailMessage);
			log.info("Email notification sent for contract: {}", contract.getContractId());
		} catch (Exception e) {
			log.error("Failed to send email for contract: {}", contract.getContractId(), e);
		}
	}

	private String createEmailContent(Contract contract, String message) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		return String.format("""
				%s

				Contract Details:
				ID: %s
				Name: %s
				Liquidation Date: %s


				This is an automated message.
				""",
				message,
				contract.getContractId(),
				contract.getName(),
				contract.getLiquidationDate().atZone(java.time.ZoneId.systemDefault()).format(formatter));
	}
}
