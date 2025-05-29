package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.Contract;
import com.example.demo.domain.response.ResultPaginationDTO;
import com.example.demo.service.ContractService;
import com.example.demo.util.annotation.ApiMessage;
import com.example.demo.util.constant.EnumStatus;
import com.example.demo.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class ContractController {
	private final ContractService contractService;

	@PostMapping("/contracts")
	@ApiMessage("Create a contract")
	public ResponseEntity<Contract> createrContract(@Valid @RequestBody Contract postmanContract)
			throws IdInvalidException {

		Contract currContract = this.contractService.handleFindContractById(postmanContract.getId());
		if (currContract != null) {
			throw new IdInvalidException("Contract ID = " + postmanContract.getId() + " already exists");
		}
		postmanContract.setStatus(EnumStatus.UNLIQUIDATED);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(this.contractService.handleSaveContract(postmanContract));
	}

	@PutMapping("/contracts")
	@ApiMessage("Update a contract")
	public ResponseEntity<Contract> updateContract(@RequestBody Contract postmanContract) throws IdInvalidException {
		Contract currContract = this.contractService.handleFindContractById(postmanContract.getId());
		if (currContract == null) {
			throw new IdInvalidException("Contract ID = " + postmanContract.getId() + " doesn't exist!");
		}
		currContract.setStatus(postmanContract.getStatus());
		return ResponseEntity.status(HttpStatus.OK)
				.body(this.contractService.handleSaveContract(currContract));
	}

	@GetMapping("/contracts/{id}")
	@ApiMessage("Get a contract by ID")
	public ResponseEntity<Contract> getContractById(@PathVariable("id") String id) throws IdInvalidException {
		Contract currContract = this.contractService.handleFindContractById(id);
		if (currContract == null) {
			throw new IdInvalidException("Contract ID = " + id + " doesn't exist!");
		}
		return ResponseEntity.ok(currContract);
	}

	@GetMapping("/contracts")
	@ApiMessage("Get all contracts")
	public ResponseEntity<ResultPaginationDTO> getAllContracts(@Filter Specification<Contract> specification,
			Pageable pageable) {
		return ResponseEntity.ok(this.contractService.handleFetchAllContracts(specification, pageable));
	}

}
