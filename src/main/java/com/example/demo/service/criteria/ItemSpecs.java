package com.example.demo.service.criteria;

import org.springframework.data.jpa.domain.Specification;

import com.example.demo.domain.Item;
import com.example.demo.domain.Item_;

public class ItemSpecs {
	public static Specification<Item> matchDeletedFalse() {
		return (root, query, criteriaBuilder) -> criteriaBuilder.isFalse(root.get(Item_.DELETED));
	}
}
