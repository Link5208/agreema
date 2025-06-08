package com.example.demo.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.domain.Contract;
import com.example.demo.domain.response.ResultPaginationDTO;
import com.example.demo.util.error.IdInvalidException;
import com.example.demo.util.error.StorageException;

import jakarta.servlet.http.HttpServletResponse;

public interface ContractService {
	public Contract findContractById(long id);

	public Contract handleSaveContract(Contract contract);

	public List<Contract> findContractByContractId(String contractId);

	public ResultPaginationDTO handleFetchAllContracts(Specification<Contract> specification, Pageable pageable);

	public Contract handleCreateContract(Contract postmanContract, List<MultipartFile> files)
			throws IdInvalidException, StorageException, URISyntaxException, IOException;

	public Contract handleUpdateContract(Contract postmanContract) throws IdInvalidException;

	public Contract handleGetContractById(long id) throws IdInvalidException;

	public void handleDelete(long id) throws IdInvalidException;

	public void handleExportToExcel(HttpServletResponse response) throws IOException;

	public void handleAutoLiquidation(Instant liquidationDate);
}
