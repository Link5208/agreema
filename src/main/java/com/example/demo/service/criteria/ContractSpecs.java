package com.example.demo.service.criteria;

import org.springframework.data.jpa.domain.Specification;

import com.example.demo.domain.Contract;
import com.example.demo.domain.Contract_;

public class ContractSpecs {
	public static Specification<Contract> matchDeletedFalse() {
		return (root, query, criteriaBuilder) -> criteriaBuilder.isFalse(root.get(Contract_.DELETED));
	}
}
