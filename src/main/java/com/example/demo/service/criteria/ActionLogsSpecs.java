package com.example.demo.service.criteria;

import org.springframework.data.jpa.domain.Specification;

import com.example.demo.domain.ActionLog;
import com.example.demo.domain.ActionLog_;

public class ActionLogsSpecs {
	public static Specification<ActionLog> matchDeletedFalse() {
		return (root, query, criteriaBuilder) -> criteriaBuilder.isFalse(root.get(ActionLog_.DELETED));
	}
}
