package com.practise.team1.practiseapi.support;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.practise.team1.practiseapi.model.Company;
import com.practise.team1.practiseapi.service.CompanyService;
import com.practise.team1.practiseapi.web.dto.CompanyDTO;

@Component
public class CompanyDTOToCompany implements Converter<CompanyDTO, Company> {

	@Autowired
	CompanyService companyService;

	@Override
	public Company convert(CompanyDTO dto) {

		Company company = new Company();

		if (dto.getId() != null) {
			company = companyService.findOne(dto.getId());

			if (company == null) {
				throw new IllegalStateException("Tried to modify a non-existant company");
			}
		}

		company.setId(dto.getId());
		company.setName(dto.getName());
		company.setAddress(dto.getAddress());
		company.setContactPerson(dto.getContactPerson());

		Date date = null;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String validLicenceTill = dto.getValidLicenceTill();
		try {
			date = sdf.parse(validLicenceTill);
		} catch (ParseException e) {
			System.out.println("Exception occured during recording of date!");
			e.printStackTrace();
		}

		if (date != null)
			company.setValidLicenceTill(date);

		return company;
	}

	public List<Company> convert(List<CompanyDTO> dtos) {

		List<Company> companies = new ArrayList<>();

		for (CompanyDTO dto : dtos) {
			companies.add(convert(dto));
		}

		return companies;

	}

}
