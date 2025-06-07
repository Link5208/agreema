package com.example.demo.domain;

import java.time.Instant;

import com.example.demo.util.SecurityUtil;
import com.example.demo.util.constant.EnumTypeLog;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "action_logs")
@Getter
@Setter
@NoArgsConstructor
public class ActionLog {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String actionLogId;

	@ManyToOne
	@JoinColumn(name = "contract_id")
	@NotNull(message = "Contract_id must not blank!")
	@JsonIgnore
	private Contract contract;

	private EnumTypeLog type;

	private boolean deleted;

	/**
	 * @param actionLogId
	 * @param contract
	 * @param type
	 * @param deleted
	 */
	public ActionLog(String actionLogId, @NotNull(message = "Contract_id must not blank!") Contract contract,
			EnumTypeLog type, boolean deleted) {
		this.actionLogId = actionLogId;
		this.contract = contract;
		this.type = type;
		this.deleted = deleted;
	}

	private Instant createdAt;
	private Instant updatedAt;
	private String createdBy;
	private String updatedBy;

	@PrePersist
	public void handleBeforeCreate() {
		this.createdAt = Instant.now();
		this.createdBy = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
	}

	@PreUpdate
	public void handleBeforeUpdate() {
		this.updatedAt = Instant.now();
		this.updatedBy = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";

	}
}
