package com.example.demo.service.criteria;

import org.springframework.data.jpa.domain.Specification;

import com.example.demo.domain.FileInfo;
import com.example.demo.domain.FileInfo_;

public class FileSpecs {
	public static Specification<FileInfo> matchDeletedFalse() {
		return (root, query, criteriaBuilder) -> criteriaBuilder.isFalse(root.get(FileInfo_.DELETED));
	}
}
