package com.example.demo.service.impl;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.demo.domain.Item;
import com.example.demo.domain.response.ResultPaginationDTO;
import com.example.demo.repository.ItemRepository;
import com.example.demo.service.ItemService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
	private final ItemRepository itemRepository;

	public Item handleSaveItem(Item item) {
		return this.itemRepository.save(item);
	}

	public Item handleFetchById(long id) {
		Optional<Item> optional = this.itemRepository.findById(id);
		return optional.isPresent() ? optional.get() : null;
	}

	public ResultPaginationDTO handleFetchAllItems(Specification<Item> specification, Pageable pageable) {
		Page<Item> page = this.itemRepository.findAllByDeletedFalse(specification, pageable);
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

	public void handleDelete(long id) {
		this.itemRepository.deleteById(id);
	}
}
