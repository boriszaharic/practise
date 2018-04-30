package com.practise.team1.practiseapi.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.practise.team1.practiseapi.model.Category;
import com.practise.team1.practiseapi.repository.CategoryRepository;
import com.practise.team1.practiseapi.service.CategoryService;

@Service
@Transactional
public class JpaCategoryService implements CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public Page<Category> findAll(int pageNum) {

		return categoryRepository.findAll(PageRequest.of(pageNum, 10));
	}

	@Override
	public Category findOne(Long id) {

		return categoryRepository.getOne(id);
	}

	@Override
	public void save(Category category) {

		categoryRepository.save(category);

	}

	@Override
	public void remove(Long id) {

		categoryRepository.deleteById(id);

	}

	@Override
	public Page<Category> findByCompanyId(int pageNum, Long companyId) {

		return categoryRepository.findByCompanyId((PageRequest.of(pageNum, 10)),companyId);
	}

}
