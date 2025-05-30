package com.example.demo.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.Contract;
import com.example.demo.domain.Item;
import com.example.demo.domain.response.ResultPaginationDTO;
import com.example.demo.service.ContractService;
import com.example.demo.service.ItemService;
import com.example.demo.util.annotation.ApiMessage;
import com.example.demo.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class ItemController {
	private final ItemService itemService;
	private final ContractService contractService;

	@PostMapping("/items")
	@ApiMessage("Create item")
	public ResponseEntity<Item> createItem(@Valid @RequestBody Item postmanItem)
			throws IdInvalidException {

		Item currItem = this.itemService.handleFetchById(postmanItem.getId());
		if (currItem != null) {
			throw new IdInvalidException("Item ID = " + postmanItem.getId() + " already exists");
		}

		postmanItem.setTotal(postmanItem.getQuantity() * postmanItem.getPrice());
		postmanItem.setDeleted(false);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(this.itemService.handleSaveItem(postmanItem));
	}

	@PutMapping("/items")
	@ApiMessage("Update an item")
	public ResponseEntity<Item> updateItem(@RequestBody Item postmanItem) throws IdInvalidException {
		Item currItem = this.itemService.handleFetchById(postmanItem.getId());
		if (currItem == null) {
			throw new IdInvalidException("Item ID = " + postmanItem.getId() + " doesn't exist!");
		}
		currItem.setName(postmanItem.getName());
		currItem.setUnit(postmanItem.getUnit());
		currItem.setQuantity(postmanItem.getQuantity());
		currItem.setPrice(postmanItem.getPrice());
		postmanItem.setTotal(postmanItem.getQuantity() * postmanItem.getPrice());

		if (postmanItem.getContract() != null) {
			Contract currContract = this.contractService.findContractById(postmanItem.getContract().getId());
			currItem.setContract(currContract);
		}
		return ResponseEntity.status(HttpStatus.OK)
				.body(this.itemService.handleSaveItem(currItem));
	}

	@GetMapping("/items/{id}")
	@ApiMessage("Get an item by ID")
	public ResponseEntity<Item> getItemById(@PathVariable("id") long id) throws IdInvalidException {
		Item currItem = this.itemService.handleFetchById(id);
		if (currItem == null) {
			throw new IdInvalidException("Item ID = " + id + " doesn't exist!");
		}
		return ResponseEntity.ok(currItem);
	}

	@GetMapping("/items")
	@ApiMessage("Get all items")
	public ResponseEntity<ResultPaginationDTO> getAllItems(@Filter Specification<Item> specification,
			Pageable pageable) {
		return ResponseEntity.ok(this.itemService.handleFetchAllItems(specification, pageable));
	}

	@DeleteMapping("/items/{id}")
	@ApiMessage("Delete an item")
	public ResponseEntity<Void> deleteItem(@PathVariable("id") long id) throws IdInvalidException {
		Item currItem = this.itemService.handleFetchById(id);
		if (currItem == null) {
			throw new IdInvalidException("Item ID = " + id + " doesn't exist!");
		}
		currItem.setDeleted(true);
		this.itemService.handleSaveItem(currItem);
		return ResponseEntity.ok(null);
	}

}
