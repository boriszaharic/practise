package com.practise.team1.practiseapi.service;

import org.springframework.data.domain.Page;

import com.practise.team1.practiseapi.model.Company;

public interface CompanyService {

	Page<Company> findAll(int pageNum);

	Company findOne(Long id);

	void save(Company company);

	void remove(Long id);
}
