package com.example.demo.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.demo.domain.Contract;
import com.example.demo.domain.response.ResultPaginationDTO;
import com.example.demo.repository.ContractRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ContractService {
	private final ContractRepository contractRepository;

	public Contract handleFindContractById(long id) {
		Optional<Contract> optional = this.contractRepository.findById(id);
		return optional.isPresent() ? optional.get() : null;
	}

	public Contract handleSaveContract(Contract contract) {
		return this.contractRepository.save(contract);
	}

	public ResultPaginationDTO handleFetchAllContracts(Specification<Contract> specification, Pageable pageable) {
		Page<Contract> page = this.contractRepository.findAll(specification, pageable);
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
