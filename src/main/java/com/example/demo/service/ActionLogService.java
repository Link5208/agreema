package com.example.demo.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.demo.domain.ActionLog;
import com.example.demo.domain.response.ResultPaginationDTO;
import com.example.demo.repository.ActionLogRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ActionLogService {
	private final ActionLogRepository actionLogRepository;

	public ActionLog handleSaveActionLog(ActionLog actionLog) {
		return this.actionLogRepository.save(actionLog);
	}

	public ResultPaginationDTO handleFetchAllActionLogs(Specification<ActionLog> specification, Pageable pageable) {
		Page<ActionLog> page = this.actionLogRepository.findAll(specification, pageable);
		ResultPaginationDTO result = new ResultPaginationDTO();
		ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

		meta.setPage(pageable.getPageNumber() + 1);
		meta.setPageSize(pageable.getPageSize());
		meta.setPages(page.getTotalPages());
		meta.setTotal(page.getTotalElements());

		result.setMeta(meta);

		List<ActionLog> list = page.getContent().stream().filter(actionLog -> !actionLog.isDeleted()).toList();

		result.setResult(list);

		return result;
	}
}
