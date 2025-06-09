package com.example.demo.service.impl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.domain.Contract;
import com.example.demo.domain.FileInfo;
import com.example.demo.domain.response.ResultPaginationDTO;
import com.example.demo.repository.ContractRepository;
import com.example.demo.service.ActionLogService;
import com.example.demo.service.ContractService;
import com.example.demo.service.FileService;
import com.example.demo.service.ItemService;
import com.example.demo.service.criteria.ContractSpecs;
import com.example.demo.util.constant.EnumStatus;
import com.example.demo.util.constant.EnumTypeLog;
import com.example.demo.util.error.IdInvalidException;
import com.example.demo.util.error.StorageException;
import com.example.demo.util.excel.ContractExcelGenerator;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ContractServiceImpl implements ContractService {
	private final ContractRepository contractRepository;
	private final ActionLogService actionLogService;
	private final ItemService itemService;
	private final FileService fileService;

	public Contract findContractById(long id) {
		Optional<Contract> optional = this.contractRepository.findById(id);
		return optional.isPresent() ? optional.get() : null;
	}

	public Contract handleSaveContract(Contract contract) {
		return this.contractRepository.save(contract);
	}

	public List<Contract> findContractByContractId(String contractId) {
		List<Contract> contracts = this.contractRepository.findByContractId(contractId);
		return contracts.stream().filter(contract -> !contract.isDeleted()).toList();
	}

	public ResultPaginationDTO handleFetchAllContracts(Specification<Contract> specification, Pageable pageable) {
		Specification<Contract> finalSpec = ContractSpecs.matchDeletedFalse();
		if (specification != null) {
			finalSpec = finalSpec.and(specification);
		}
		Page<Contract> page = this.contractRepository.findAll(finalSpec, pageable);
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

	@Transactional(rollbackFor = Exception.class)
	public Contract handleCreateContract(Contract postmanContract, List<MultipartFile> files)
			throws IdInvalidException, StorageException, URISyntaxException, IOException {

		if (this.contractRepository.existsByContractIdAndDeletedFalse(postmanContract.getContractId())) {
			throw new IdInvalidException("Contract ID = " + postmanContract.getId() + " already exists");
		}
		if (postmanContract.getStatus() == null) {
			postmanContract.setStatus(EnumStatus.UNLIQUIDATED);
		}
		postmanContract.setDeleted(false);
		Contract contract = handleSaveContract(postmanContract);

		if (postmanContract.getItems() != null && !postmanContract.getItems().isEmpty()) {
			postmanContract.getItems().forEach(item -> {
				item.setContract(contract);
				item.setTotal(item.getPrice() * item.getQuantity());
				itemService.handleSaveItem(item);
			});
		}

		// Handle files
		if (files != null && !files.isEmpty()) {
			for (MultipartFile file : files) {
				FileInfo fileInfo = new FileInfo();
				fileInfo.setFileId(contract.getContractId() + "-" + System.currentTimeMillis());
				fileInfo.setContract(contract);

				fileService.handleUpload(file, fileInfo);
			}
		}

		this.actionLogService.handleCreateActionLog(contract, EnumTypeLog.CREATE_CONTRACT);
		return contract;
	}

	public Contract handleUpdateContract(Contract postmanContract) throws IdInvalidException {
		Contract currContract = findContractById(postmanContract.getId());
		if (currContract == null) {
			throw new IdInvalidException("Contract ID = " + postmanContract.getId() + " doesn't exist!");
		}
		if (currContract.getStatus() != postmanContract.getStatus()) {
			this.actionLogService.handleCreateActionLog(postmanContract, EnumTypeLog.CHANGE_CONTRACT);
			currContract.setStatus(postmanContract.getStatus());
		}

		return handleSaveContract(currContract);
	}

	public Contract handleGetContractById(long id) throws IdInvalidException {
		Contract currContract = findContractById(id);
		if (currContract == null) {
			throw new IdInvalidException("Contract ID = " + id + " doesn't exist!");
		}
		return currContract;
	}

	public void handleDelete(long id) throws IdInvalidException {
		Contract currContract = findContractById(id);
		if (currContract == null) {
			throw new IdInvalidException("Contract ID = " + id + " doesn't exist!");
		}
		currContract.getItems().forEach(item -> {
			try {
				this.itemService.handleDelete(item.getId());
			} catch (IdInvalidException e) {

			}
		});

		currContract.setDeleted(true);
		handleSaveContract(currContract);
		this.actionLogService.handleCreateActionLog(currContract, EnumTypeLog.DELETE_CONTRACT);
	}

	public void handleExportToExcel(HttpServletResponse response) throws IOException {
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=contracts.xlsx";
		response.setHeader(headerKey, headerValue);
		List<Contract> contracts = this.contractRepository.findAll(ContractSpecs.matchDeletedFalse());
		ContractExcelGenerator generator = new ContractExcelGenerator(contracts);
		generator.generateExcelFile(response);
	}

	@Transactional
	public void handleAutoLiquidation(Instant liquidationDate) {
		List<Contract> contracts = contractRepository.findByStatusAndSignDateBeforeAndDeletedFalse(
				EnumStatus.UNLIQUIDATED,
				liquidationDate);

		for (Contract contract : contracts) {
			contract.setStatus(EnumStatus.LIQUIDATED);
			handleSaveContract(contract);
			this.actionLogService.handleCreateActionLog(
					contract,
					EnumTypeLog.AUTO_LIQUIDATE_CONTRACT);

		}
	}
}
