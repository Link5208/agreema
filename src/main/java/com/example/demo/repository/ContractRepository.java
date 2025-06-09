package com.example.demo.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.Contract;
import com.example.demo.util.constant.EnumStatus;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long>, JpaSpecificationExecutor<Contract> {
	Page<Contract> findAll(Specification<Contract> specification, Pageable pageable);

	List<Contract> findByContractId(String contractId);

	boolean existsByContractIdAndDeletedFalse(String contractId);

	Optional<Contract> findByContractIdAndDeletedFalse(String contractId);

	List<Contract> findByLiquidationDateLessThanEqualAndStatusAndDeletedFalse(
			Instant liquidationDate,
			EnumStatus status);

	List<Contract> findByLiquidationDateEqualsAndStatusAndDeletedFalse(Instant liquidationDate, EnumStatus unliquidated);

}
