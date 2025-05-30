package com.example.demo.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.example.demo.domain.ActionLog;
import com.example.demo.domain.response.ResultPaginationDTO;

public interface ActionLogService {
	public ActionLog handleSaveActionLog(ActionLog actionLog);

	public ResultPaginationDTO handleFetchAllActionLogs(Specification<ActionLog> specification, Pageable pageable);

}
