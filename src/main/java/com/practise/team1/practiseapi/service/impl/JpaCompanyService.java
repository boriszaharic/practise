package com.practise.team1.practiseapi.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.practise.team1.practiseapi.model.Company;
import com.practise.team1.practiseapi.repository.CompanyRepository;
import com.practise.team1.practiseapi.service.CompanyService;

@Service
@Transactional
public class JpaCompanyService implements CompanyService {

	@Autowired
	private CompanyRepository companyRepository;

	@Override
	public Page<Company> findAll(int pageNum) {

		return companyRepository.findAll(PageRequest.of(pageNum, 5));
	}

	@Override
	public Company findOne(Long id) {

		return companyRepository.getOne(id);
	}

	@Override
	public void save(Company company) {

		companyRepository.save(company);

	}

	@Override
	public void remove(Long id) {

		companyRepository.deleteById(id);

	}

}
