package com.practise.team1.practiseapi.web.dto;

public class CompanyDTO {

	private Long id;

	private String name;

	private String address;

	private String validLicenceTill;

	private String contactPerson;

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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getValidLicenceTill() {
		return validLicenceTill;
	}

	public void setValidLicenceTill(String validLicenceTill) {
		this.validLicenceTill = validLicenceTill;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

}
