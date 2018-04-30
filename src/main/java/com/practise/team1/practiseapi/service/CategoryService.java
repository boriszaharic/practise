package com.practise.team1.practiseapi.service;

import org.springframework.data.domain.Page;

import com.practise.team1.practiseapi.model.Category;

public interface CategoryService {

	Page<Category> findAll(int pageNum);

	Category findOne(Long id);

	void save(Category category);

	void remove(Long id);

	Page<Category> findByCompanyId(int pageNum, Long companyId);
}
