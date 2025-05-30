package com.example.demo.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.example.demo.domain.Item;
import com.example.demo.domain.response.ResultPaginationDTO;

public interface ItemService {
	public Item handleSaveItem(Item item);

	public Item handleFetchById(long id);

	public ResultPaginationDTO handleFetchAllItems(Specification<Item> specification, Pageable pageable);

	public void handleDelete(long id);
}
