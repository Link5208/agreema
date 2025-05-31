package com.example.demo.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.demo.domain.ActionLog;
import com.example.demo.domain.Contract;
import com.example.demo.domain.Counter;
import com.example.demo.domain.response.ResultPaginationDTO;
import com.example.demo.repository.ActionLogRepository;
import com.example.demo.service.ActionLogService;
import com.example.demo.service.criteria.ActionLogsSpecs;
import com.example.demo.util.constant.EnumTypeLog;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ActionLogServiceImpl implements ActionLogService {
	private final ActionLogRepository actionLogRepository;

	public ActionLog handleSaveActionLog(ActionLog actionLog) {
		return this.actionLogRepository.save(actionLog);
	}

	public ResultPaginationDTO handleFetchAllActionLogs(Specification<ActionLog> specification, Pageable pageable) {
		Specification<ActionLog> finalSpec = ActionLogsSpecs.matchDeletedFalse();
		if (specification != null) {
			finalSpec = finalSpec.and(specification);
		}
		Page<ActionLog> page = this.actionLogRepository.findAllByDeletedFalse(finalSpec, pageable);
		ResultPaginationDTO result = new ResultPaginationDTO();
		ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

		meta.setPage(pageable.getPageNumber() + 1);
		meta.setPageSize(pageable.getPageSize());
		meta.setPages(page.getTotalPages());
		meta.setTotal(page.getTotalElements());

		result.setMeta(meta);

		result.setResult(page.getContent());

		return result;
	}

	public ActionLog handleCreateActionLog(Contract contract, EnumTypeLog type) {
		long id = Counter.getAndIncrement();
		String formattedId = String.format("%03d", id);
		String actionLogId = "AL" + formattedId;
		return handleSaveActionLog(new ActionLog(actionLogId, contract, type, false));
	}
}
