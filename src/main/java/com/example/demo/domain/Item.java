package com.example.demo.domain;

import java.time.Instant;

import com.example.demo.util.SecurityUtil;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Items")
@Getter
@Setter
@NoArgsConstructor
public class Item {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@NotBlank(message = "Item-id must not blank!")
	private String itemId;

	@NotBlank(message = "Item-name must not blank!")
	private String name;

	@NotBlank(message = "Unit must not blank!")
	private String unit;

	@NotNull(message = "Quantity must not blank!")
	private long quantity;

	@NotNull(message = "Unit price must not blank!")
	private double price;

	private double total;
	private boolean deleted;

	/**
	 * @param itemId
	 * @param name
	 * @param unit
	 * @param quantity
	 * @param price
	 * @param total
	 * @param deleted
	 * @param contract
	 */
	public Item(@NotBlank(message = "Item-id must not blank!") String itemId,
			@NotBlank(message = "Item-name must not blank!") String name,
			@NotBlank(message = "Unit must not blank!") String unit,
			@NotNull(message = "Quantity must not blank!") long quantity,
			@NotNull(message = "Unit price must not blank!") double price, double total, boolean deleted,
			@NotNull(message = "Contract_id must not blank!") Contract contract) {
		this.itemId = itemId;
		this.name = name;
		this.unit = unit;
		this.quantity = quantity;
		this.price = price;
		this.total = total;
		this.deleted = deleted;
		this.contract = contract;
	}

	@ManyToOne
	@JoinColumn(name = "contract_id")
	@NotNull(message = "Contract_id must not blank!")
	private Contract contract;

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
