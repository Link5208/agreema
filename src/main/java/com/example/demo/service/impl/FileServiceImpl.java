package com.example.demo.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.domain.Contract;
import com.example.demo.domain.FileInfo;
import com.example.demo.domain.response.ResultPaginationDTO;
import com.example.demo.repository.ContractRepository;
import com.example.demo.repository.FileRepository;
import com.example.demo.service.FileService;
import com.example.demo.service.criteria.FileSpecs;
import com.example.demo.util.error.IdInvalidException;
import com.example.demo.util.error.StorageException;

@Repository

public class FileServiceImpl implements FileService {
	private final FileRepository fileRepository;
	private final ContractRepository contractRepository;

	/**
	 * @param fileRepository
	 * @param contractRepository
	 */
	public FileServiceImpl(FileRepository fileRepository, ContractRepository contractRepository) {
		this.fileRepository = fileRepository;
		this.contractRepository = contractRepository;
	}

	@Value("${hoanglong.upload-file.base-uri}")
	private String baseURI;

	public void createDirectory(String folder) throws URISyntaxException {
		URI uri = new URI(folder);
		Path path = Paths.get(uri);
		File file = new File(path.toString());
		if (!file.isDirectory()) {
			try {
				Files.createDirectory(file.toPath());
				System.out.println(">>> CREATE NEW DIRECTORY SUCCESSFULLY, PATH = " + file.toPath());
			} catch (IOException e) {

				e.printStackTrace();
			}
		} else {
			System.out.println(">>> SKIP MAKING DIRECTORY, ALREADY EXISTED");
		}

	}

	public String store(MultipartFile file) throws URISyntaxException, IOException {
		String finalName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
		URI uri = new URI(baseURI + "/" + finalName);
		Path path = Paths.get(uri);
		try (InputStream inputStream = file.getInputStream()) {
			Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
		}
		return finalName;
	}

	public long getFileLength(String fileName) throws URISyntaxException {
		URI uri = new URI(baseURI + "/" + fileName);
		Path path = Paths.get(uri);
		File file = new File(path.toString());

		if (!file.exists() || file.isDirectory()) {
			return 0;
		}
		return file.length();
	}

	public InputStreamResource getResource(String fileName)
			throws URISyntaxException, FileNotFoundException {
		// TODO Auto-generated method stub
		URI uri = new URI(baseURI + "/" + fileName);
		Path path = Paths.get(uri);

		File file = new File(path.toString());
		return new InputStreamResource(new FileInputStream(file));
	}

	public FileInfo handleUpload(MultipartFile file, FileInfo postmanFileInfo)
			throws IdInvalidException, StorageException, URISyntaxException, IOException {
		if (fileRepository.existsByFileIdAndDeletedFalse(postmanFileInfo.getFileId())) {
			throw new IdInvalidException("Item ID = " + postmanFileInfo.getFileId() + "already exists");
		}
		Contract contract = this.contractRepository
				.findByContractIdAndDeletedFalse(postmanFileInfo.getContract().getContractId()).orElse(null);
		if (contract == null) {
			throw new IdInvalidException(
					"Contract ID = " + postmanFileInfo.getContract().getContractId() + " doesn't exist!");
		}
		postmanFileInfo.setContract(contract);

		if (file == null || file.isEmpty()) {
			throw new StorageException("File is empty. Please upload a file");
		}
		String fileName = file.getOriginalFilename().replace("%20", " ");

		postmanFileInfo.setName(fileName);

		List<String> allowedExtensions = Arrays.asList("pdf", "jpg", "jpeg", "png", "doc", "docx");
		boolean isValid = allowedExtensions.stream().anyMatch(extension -> fileName.toLowerCase().endsWith(extension));
		if (!isValid) {
			throw new StorageException("Invalid file extension. Only allow " + allowedExtensions.toString());
		}

		String tail = "";
		if (fileName != null && fileName.contains(".")) {
			tail = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
		}
		postmanFileInfo.setType(tail);

		createDirectory(baseURI);
		String uploadFile = store(file);
		postmanFileInfo.setSize(getFileLength(uploadFile));
		postmanFileInfo.setDeleted(false);
		postmanFileInfo.setPath("/upload/" + uploadFile);

		return this.fileRepository.save(postmanFileInfo);

	}

	public void handleDelete(long id) throws IdInvalidException {
		FileInfo fileInfo = this.fileRepository.findById(id).orElse(null);
		if (fileInfo == null) {
			throw new IdInvalidException("File ID = " + id + " doesn't exist!");
		}
		fileInfo.setDeleted(true);
		this.fileRepository.save(fileInfo);
	}

	public ResultPaginationDTO handleFetchAllFiles(Specification<FileInfo> specification, Pageable pageable) {
		Specification<FileInfo> finalSpec = FileSpecs.matchDeletedFalse();
		if (specification != null) {
			finalSpec = finalSpec.and(specification);
		}
		Page<FileInfo> page = this.fileRepository.findAll(finalSpec, pageable);
		ResultPaginationDTO result = new ResultPaginationDTO();
		ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

		meta.setPage(pageable.getPageNumber() + 1);
		meta.setPageSize(pageable.getPageSize());
		meta.setPages(page.getTotalPages());
		meta.setTotal(page.getTotalElements());

		result.setMeta(meta);

		result.setResult(page.getContent());

		return result;
	}

}