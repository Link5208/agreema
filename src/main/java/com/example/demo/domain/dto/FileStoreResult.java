package com.example.demo.domain.dto;

public class FileStoreResult {
	private String fileName;
	private String checksum;

	public FileStoreResult(String fileName, String checksum) {
		this.fileName = fileName;
		this.checksum = checksum;
	}

	public String getFileName() {
		return fileName;
	}

	public String getChecksum() {
		return checksum;
	}
}
