package com.example.demo.domain;

import java.time.Instant;
import java.util.List;

import com.example.demo.util.SecurityUtil;
import com.example.demo.util.constant.EnumStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "contracts")
@Getter
@Setter
@NoArgsConstructor
public class Contract {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@NotBlank(message = "Contract-id must not blank!")
	private String contractId;

	@NotBlank(message = "Contract-name must not blank!")
	private String name;

	@NotNull(message = "Sign date must not be null!")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
	private Instant signDate;

	private EnumStatus status;
	private boolean deleted;

	private Instant createdAt;
	private Instant updatedAt;
	private String createdBy;
	private String updatedBy;

	/**
	 * @param contractId
	 * @param name
	 * @param signDate
	 * @param status
	 * @param deleted
	 */
	public Contract(@NotBlank(message = "Contract-id must not blank!") String contractId,
			@NotBlank(message = "Contract-name must not blank!") String name,
			@NotNull(message = "Sign date must not be null!") Instant signDate, EnumStatus status, boolean deleted) {
		this.contractId = contractId;
		this.name = name;
		this.signDate = signDate;
		this.status = status;
		this.deleted = deleted;
	}

	@OneToMany(mappedBy = "contract", fetch = FetchType.LAZY)
	private List<Item> items;

	@OneToMany(mappedBy = "contract", fetch = FetchType.LAZY)
	@JsonIgnore
	private List<ActionLog> actionLogs;

	@OneToMany(mappedBy = "contract", fetch = FetchType.LAZY)
	@JsonIgnore
	private List<FileInfo> files;

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
