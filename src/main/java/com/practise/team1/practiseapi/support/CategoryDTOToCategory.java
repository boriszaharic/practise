package com.practise.team1.practiseapi.support;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.practise.team1.practiseapi.model.Category;
import com.practise.team1.practiseapi.service.CategoryService;
import com.practise.team1.practiseapi.service.CompanyService;
import com.practise.team1.practiseapi.web.dto.CategoryDTO;

@Component
public class CategoryDTOToCategory implements Converter<CategoryDTO, Category> {

	@Autowired
	CategoryService categoryService;

	@Autowired
	CompanyService companyService;

	@Override
	public Category convert(CategoryDTO dto) {

		Category category = new Category();

		if (dto.getId() != null) {
			category = categoryService.findOne(dto.getId());

			if (category == null) {
				throw new IllegalStateException("Tried to modify a non-existant category");
			}
		}

		category.setId(dto.getId());
		category.setName(dto.getName());
		category.setCompany(companyService.findOne(dto.getCompanyId()));

		return category;
	}

	public List<Category> convert(List<CategoryDTO> dtos) {

		List<Category> categories = new ArrayList<>();

		for (CategoryDTO dto : dtos) {
			categories.add(convert(dto));
		}

		return categories;

	}

}
