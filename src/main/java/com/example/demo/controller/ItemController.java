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

import com.example.demo.domain.Item;
import com.example.demo.domain.response.ResultPaginationDTO;
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

	@PostMapping("/items")
	@ApiMessage("Create item")
	public ResponseEntity<Item> createItem(@Valid @RequestBody Item postmanItem)
			throws IdInvalidException {

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(this.itemService.handleCreateItem(postmanItem));
	}

	@PutMapping("/items")
	@ApiMessage("Update an item")
	public ResponseEntity<Item> updateItem(@RequestBody Item postmanItem) throws IdInvalidException {

		return ResponseEntity.status(HttpStatus.OK)
				.body(this.itemService.handleUpdateItem(postmanItem));
	}

	@GetMapping("/items/{id}")
	@ApiMessage("Get an item by ID")
	public ResponseEntity<Item> getItemById(@PathVariable("id") long id) throws IdInvalidException {

		return ResponseEntity.ok(this.itemService.handleFetchItemById(id));
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
		this.itemService.handleDelete(id);
		return ResponseEntity.ok(null);
	}

}
