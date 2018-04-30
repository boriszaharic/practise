package com.practise.team1.practiseapi.web.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class InventoryItemDTO {

	private Long id;

	private String name;

	private Integer orderNumber;

	private Float value;

	private String description;

	private Integer barcode;

	@JsonIgnore
	private Long categoryId;

	@JsonIgnore
	private String categoryName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(Integer orderNumber) {
		this.orderNumber = orderNumber;
	}

	public Float getValue() {
		return value;
	}

	public void setValue(Float value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getBarcode() {
		return barcode;
	}

	public void setBarcode(Integer barcode) {
		this.barcode = barcode;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public List<String> toStringAll() {
		
		List<String> raw = new ArrayList<>();
		
		raw.add(this.getId().toString());
		raw.add(this.getName());
		raw.add(this.getOrderNumber().toString());
		raw.add(this.getValue().toString());
		raw.add(this.getDescription());
		raw.add(this.getBarcode().toString());
		
		return raw;
	}

}
