package com.practise.team1.practiseapi.support;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.practise.team1.practiseapi.model.Company;
import com.practise.team1.practiseapi.web.dto.CompanyDTO;

@Component
public class CompanyToCompanyDTO implements Converter<Company, CompanyDTO> {

	@Override
	public CompanyDTO convert(Company company) {

		CompanyDTO dto = new CompanyDTO();

		dto.setId(company.getId());

		dto.setName(company.getName());

		dto.setAddress(company.getAddress());

		dto.setContactPerson(company.getContactPerson());

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		dto.setValidLicenceTill(sdf.format(company.getValidLicenceTill()));

		return dto;
	}

	public List<CompanyDTO> convert(List<Company> companies) {

		List<CompanyDTO> dtos = new ArrayList<>();

		for (Company company : companies) {
			dtos.add(convert(company));
		}

		return dtos;
	}

}
