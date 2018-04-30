package com.practise.team1.practiseapi.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class CategoryDTO {

	private Long id;

	private String name;
	
	@JsonIgnore
	private Long companyId;
	
	@JsonIgnore
	private String companyName;

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

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

}
