package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.Contract;
import com.example.demo.domain.response.ResultPaginationDTO;
import com.example.demo.service.ContractService;
import com.example.demo.util.annotation.ApiMessage;
import com.example.demo.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import java.io.IOException;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class ContractController {
	private final ContractService contractService;

	@PostMapping("/contracts")
	@ApiMessage("Create a contract")
	public ResponseEntity<Contract> createContract(@Valid @RequestBody Contract postmanContract)
			throws IdInvalidException {

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(this.contractService.handleCreateContract(postmanContract));
	}

	@PutMapping("/contracts")
	@ApiMessage("Update a contract")
	public ResponseEntity<Contract> updateContract(@RequestBody Contract postmanContract) throws IdInvalidException {

		return ResponseEntity.status(HttpStatus.OK)
				.body(this.contractService.handleUpdateContract(postmanContract));
	}

	@GetMapping("/contracts/{id}")
	@ApiMessage("Get a contract by ID")
	public ResponseEntity<Contract> getContractById(@PathVariable("id") long id) throws IdInvalidException {

		return ResponseEntity.ok(this.contractService.handleGetContractById(id));
	}

	@GetMapping("/contracts")
	@ApiMessage("Get all contracts")
	public ResponseEntity<ResultPaginationDTO> getAllContracts(@Filter Specification<Contract> specification,
			Pageable pageable) {
		return ResponseEntity.ok(this.contractService.handleFetchAllContracts(specification, pageable));
	}

	@DeleteMapping("/contracts/{id}")
	@ApiMessage("Delete a contract")
	public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
		this.contractService.handleDelete(id);
		return ResponseEntity.ok(null);
	}

	@GetMapping("/contracts/export-to-excel")
	public ResponseEntity<Void> exportToExcelFile(HttpServletResponse response) throws IOException {
		this.contractService.handleExportToExcel(response);
		return ResponseEntity.ok(null);
	}

}
