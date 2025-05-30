package com.example.demo.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.example.demo.domain.Contract;
import com.example.demo.domain.response.ResultPaginationDTO;

public interface ContractService {
	public Contract handleFindContractById(long id);

	public Contract handleSaveContract(Contract contract);

	public ResultPaginationDTO handleFetchAllContracts(Specification<Contract> specification, Pageable pageable);
}
