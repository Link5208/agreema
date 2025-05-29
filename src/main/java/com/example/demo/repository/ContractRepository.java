package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.demo.domain.Contract;

public interface ContractRepository extends JpaRepository<Contract, String>, JpaSpecificationExecutor<Contract> {

}
