package com.practise.team1.practiseapi.support;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.practise.team1.practiseapi.model.Category;
import com.practise.team1.practiseapi.web.dto.CategoryDTO;

@Component
public class CategoryToCategoryDTO implements Converter<Category, CategoryDTO> {

	@Override
	public CategoryDTO convert(Category category) {

		CategoryDTO dto = new CategoryDTO();

		dto.setId(category.getId());
		dto.setName(category.getName());
		dto.setCompanyId(category.getCompany().getId());
		dto.setCompanyName(category.getCompany().getName());

		return dto;
	}

	public List<CategoryDTO> convert(List<Category> categories) {

		List<CategoryDTO> dtos = new ArrayList<>();

		for (Category category : categories) {
			dtos.add(convert(category));
		}

		return dtos;

	}

}
