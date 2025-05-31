package com.example.demo.service.impl;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.demo.domain.Contract;
import com.example.demo.domain.Item;
import com.example.demo.domain.response.ResultPaginationDTO;
import com.example.demo.repository.ItemRepository;
import com.example.demo.service.ActionLogService;
import com.example.demo.service.ContractService;
import com.example.demo.service.ItemService;
import com.example.demo.service.criteria.ItemSpecs;
import com.example.demo.util.constant.EnumTypeLog;
import com.example.demo.util.error.IdInvalidException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
	private final ItemRepository itemRepository;
	private final ContractService contractService;
	private final ActionLogService actionLogService;

	public Item handleSaveItem(Item item) {
		return this.itemRepository.save(item);
	}

	public Item fetchById(long id) {
		Optional<Item> optional = this.itemRepository.findById(id);
		return optional.isPresent() ? optional.get() : null;
	}

	public ResultPaginationDTO handleFetchAllItems(Specification<Item> specification, Pageable pageable) {
		Specification<Item> finalSpec = ItemSpecs.matchDeletedFalse();
		if (specification != null) {
			finalSpec = finalSpec.and(specification);
		}
		Page<Item> page = this.itemRepository.findAllByDeletedFalse(finalSpec, pageable);
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

	// public void handleDelete(long id) {
	// this.itemRepository.deleteById(id);
	// }
	public void handleDelete(long id) throws IdInvalidException {
		Item currItem = fetchById(id);
		if (currItem == null) {
			throw new IdInvalidException("Item ID = " + id + " doesn't exist!");
		}
		currItem.setDeleted(true);
		this.actionLogService.handleCreateActionLog(currItem.getContract(), EnumTypeLog.CHANGE_CONTRACT);
		handleSaveItem(currItem);
	}

	public Item handleCreateItem(Item postmanItem) throws IdInvalidException {
		Item currItem = fetchById(postmanItem.getId());
		if (currItem != null) {
			throw new IdInvalidException("Item ID = " + postmanItem.getId() + " already exists");
		}

		postmanItem.setTotal(postmanItem.getQuantity() * postmanItem.getPrice());
		postmanItem.setDeleted(false);
		Contract contract = this.contractService.findContractById(postmanItem.getId());
		postmanItem.setContract(contract);
		this.actionLogService.handleCreateActionLog(postmanItem.getContract(), EnumTypeLog.CHANGE_CONTRACT);

		return handleSaveItem(postmanItem);
	}

	public Item handleUpdateItem(Item postmanItem) throws IdInvalidException {
		Item currItem = fetchById(postmanItem.getId());
		if (currItem == null) {
			throw new IdInvalidException("Item ID = " + postmanItem.getId() + " doesn't exist!");
		}
		currItem.setName(postmanItem.getName());
		currItem.setUnit(postmanItem.getUnit());
		currItem.setQuantity(postmanItem.getQuantity());
		currItem.setPrice(postmanItem.getPrice());
		currItem.setTotal(currItem.getQuantity() * currItem.getPrice());

		if (postmanItem.getContract() != null) {
			Contract currContract = this.contractService.findContractById(postmanItem.getContract().getId());
			currItem.setContract(currContract);
		}
		this.actionLogService.handleCreateActionLog(currItem.getContract(), EnumTypeLog.CHANGE_CONTRACT);

		return handleSaveItem(currItem);
	}

	public Item handleFetchItemById(long id) throws IdInvalidException {
		Item currItem = fetchById(id);
		if (currItem == null) {
			throw new IdInvalidException("Item ID = " + id + " doesn't exist!");
		}
		return currItem;
	}
}
