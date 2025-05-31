package com.example.demo.service.impl;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.demo.domain.Contract;
import com.example.demo.domain.response.ResultPaginationDTO;
import com.example.demo.repository.ContractRepository;
import com.example.demo.service.ActionLogService;
import com.example.demo.service.ContractService;
import com.example.demo.util.constant.EnumStatus;
import com.example.demo.util.constant.EnumTypeLog;
import com.example.demo.util.error.IdInvalidException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ContractServiceImpl implements ContractService {
	private final ContractRepository contractRepository;
	private final ActionLogService actionLogService;

	public Contract findContractById(long id) {
		Optional<Contract> optional = this.contractRepository.findById(id);
		return optional.isPresent() ? optional.get() : null;
	}

	public Contract handleSaveContract(Contract contract) {
		return this.contractRepository.save(contract);
	}

	public ResultPaginationDTO handleFetchAllContracts(Specification<Contract> specification, Pageable pageable) {
		Page<Contract> page = this.contractRepository.findAllByDeletedFalse(specification, pageable);
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

	public Contract handleCreateContract(Contract postmanContract) throws IdInvalidException {
		Contract currContract = findContractById(postmanContract.getId());
		if (currContract != null) {
			throw new IdInvalidException("Contract ID = " + postmanContract.getId() + " already exists");
		}
		postmanContract.setStatus(EnumStatus.UNLIQUIDATED);
		postmanContract.setDeleted(false);
		this.actionLogService.handleCreateActionLog(postmanContract, EnumTypeLog.CREATE_CONTRACT);
		return handleSaveContract(postmanContract);
	}

	public Contract handleUpdateContract(Contract postmanContract) throws IdInvalidException {
		Contract currContract = findContractById(postmanContract.getId());
		if (currContract == null) {
			throw new IdInvalidException("Contract ID = " + postmanContract.getId() + " doesn't exist!");
		}
		if (currContract.getStatus() != postmanContract.getStatus()) {
			this.actionLogService.handleCreateActionLog(postmanContract, EnumTypeLog.CHANGE_CONTRACT_STATUS);
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
		currContract.setDeleted(true);
		handleSaveContract(currContract);
		this.actionLogService.handleCreateActionLog(currContract, EnumTypeLog.DELETE_CONTRACT);
	}
}
