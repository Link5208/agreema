package com.example.demo.service;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.example.demo.domain.Item;
import com.example.demo.domain.response.ResultPaginationDTO;
import com.example.demo.util.error.IdInvalidException;

import jakarta.servlet.http.HttpServletResponse;

public interface ItemService {
	public Item handleSaveItem(Item item);

	public Item fetchById(long id);

	public List<Item> fetchByItemId(String itemId);

	public ResultPaginationDTO handleFetchAllItems(Specification<Item> specification, Pageable pageable);

	public void handleDelete(long id) throws IdInvalidException;

	public Item handleCreateItem(Item postmanItem) throws IdInvalidException;

	public Item handleUpdateItem(Item postmanItem) throws IdInvalidException;

	public Item handleFetchItemById(long id) throws IdInvalidException;

	public void handleExportToExcel(HttpServletResponse response, long id) throws IOException;
}
