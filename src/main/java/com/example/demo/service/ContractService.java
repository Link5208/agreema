package com.example.demo.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.example.demo.domain.Contract;
import com.example.demo.domain.response.ResultPaginationDTO;
import com.example.demo.util.error.IdInvalidException;

public interface ContractService {
	public Contract findContractById(long id);

	public Contract handleSaveContract(Contract contract);

	public ResultPaginationDTO handleFetchAllContracts(Specification<Contract> specification, Pageable pageable);

	public Contract handleCreateContract(Contract postmanContract) throws IdInvalidException;

	public Contract handleUpdateContract(Contract postmanContract) throws IdInvalidException;

	public Contract handleGetContractById(long id) throws IdInvalidException;

	public void handleDelete(long id) throws IdInvalidException;
}
