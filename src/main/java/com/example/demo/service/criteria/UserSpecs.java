package com.example.demo.service.criteria;

import org.springframework.data.jpa.domain.Specification;

import com.example.demo.domain.User;
import com.example.demo.domain.User_;

public class UserSpecs {
	public static Specification<User> matchDeletedFalse() {
		return (root, query, criteriaBuilder) -> criteriaBuilder.isFalse(root.get(User_.DELETED));
	}
}
