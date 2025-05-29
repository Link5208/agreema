package com.example.demo.domain;

import java.time.Instant;
import java.util.List;

import com.example.demo.util.SecurityUtil;
import com.example.demo.util.constant.EnumStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "contracts")
@Getter
@Setter
public class Contract {
	@Id
	@NotBlank(message = "Contract-id must not blank!")
	private String id;

	@NotBlank(message = "Contract-name must not blank!")
	private String name;

	@NotNull(message = "Sign date must not be null!")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Instant signDate;

	private EnumStatus status;

	private Instant createdAt;
	private Instant updatedAt;
	private String createdBy;
	private String updatedBy;

	@OneToMany(mappedBy = "contract", fetch = FetchType.LAZY)
	@JsonIgnore
	private List<Item> items;

	@OneToMany(mappedBy = "contract", fetch = FetchType.LAZY)
	@JsonIgnore
	private List<ActionLog> actionLogs;

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
