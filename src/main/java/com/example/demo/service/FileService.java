package com.example.demo.service;

import java.io.IOException;
import java.net.URISyntaxException;

import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.domain.FileInfo;
import com.example.demo.domain.response.ResultPaginationDTO;
import com.example.demo.util.error.IdInvalidException;
import com.example.demo.util.error.StorageException;

public interface FileService {
	public FileInfo handleUpload(MultipartFile file, FileInfo postmanFileInfo)
			throws IdInvalidException, StorageException, URISyntaxException, IOException;

	public void handleDelete(long id) throws IdInvalidException;

	public ResultPaginationDTO handleFetchAllFiles(Specification<FileInfo> specification, Pageable pageable);

	public InputStreamResource getResource(long id)
			throws StorageException, URISyntaxException;

	public FileInfo findById(long id) throws IdInvalidException, StorageException;
}
