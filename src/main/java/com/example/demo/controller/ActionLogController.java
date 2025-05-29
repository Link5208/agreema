package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.ActionLog;
import com.example.demo.domain.response.ResultPaginationDTO;
import com.example.demo.service.ActionLogService;
import com.example.demo.util.annotation.ApiMessage;
import com.turkraft.springfilter.boot.Filter;

import lombok.AllArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class ActionLogController {
	private final ActionLogService actionLogService;

	@GetMapping("/action-logs")
	@ApiMessage("Get all action logs")
	public ResponseEntity<ResultPaginationDTO> getAllActionLogs(@Filter Specification<ActionLog> specification,
			Pageable pageable) {
		return ResponseEntity.ok(this.actionLogService.handleFetchAllActionLogs(specification, pageable));
	}

}
