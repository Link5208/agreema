package com.example.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.ActionLog;

@Repository
public interface ActionLogRepository extends JpaRepository<ActionLog, Long>, JpaSpecificationExecutor<ActionLog> {
	Page<ActionLog> findAllByDeletedFalse(Specification<ActionLog> specification, Pageable pageable);
}
