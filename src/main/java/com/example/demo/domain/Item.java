package com.example.demo.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Items")
@Getter
@Setter
public class Item {
	@Id
	@NotBlank(message = "Contract-id must not blank!")
	private String id;

	@NotBlank(message = "Contract-name must not blank!")
	private String name;

	@NotBlank(message = "Unit must not blank!")
	private String unit;

	@NotNull(message = "Quantity must not blank!")
	private long quantity;

	@NotNull(message = "Unit price must not blank!")
	private double price;

	private double total;

	@ManyToOne
	@JoinColumn(name = "contract_id")
	private Contract contract;
}
