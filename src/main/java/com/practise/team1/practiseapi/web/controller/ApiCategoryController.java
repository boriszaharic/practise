package com.practise.team1.practiseapi.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.practise.team1.practiseapi.model.Category;
import com.practise.team1.practiseapi.model.User;
import com.practise.team1.practiseapi.service.CategoryService;
import com.practise.team1.practiseapi.service.impl.CustomUserDetailsService;
import com.practise.team1.practiseapi.support.CategoryDTOToCategory;
import com.practise.team1.practiseapi.support.CategoryToCategoryDTO;
import com.practise.team1.practiseapi.web.dto.CategoryDTO;

@RestController
@RequestMapping(value = "/company/{companyId}/categories")
@PreAuthorize("hasAnyRole('COMPANY_ADMIN')")
public class ApiCategoryController {

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private CategoryToCategoryDTO toDTO;

	@Autowired
	private CategoryDTOToCategory toCategory;

	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<CategoryDTO>> get(@PathVariable Long companyId,
			@RequestParam(defaultValue = "0") int pageNum) {

		// method for getting logged user

		User user = loggedUser();

		// user must access only his own company
		if (user.getCompany().getId() == companyId) {

			Page<Category> categories = categoryService.findByCompanyId(pageNum, companyId);

			HttpHeaders headers = new HttpHeaders();
			headers.add("totalPages", Integer.toString(categories.getTotalPages()));

			List<CategoryDTO> dtos = toDTO.convert(categories.getContent());

			return new ResponseEntity<>(dtos, headers, HttpStatus.OK);
		} else
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

	}

	// Checking logged use in order to check access privileges
	private User loggedUser() {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		String username = auth.getName();

		User user = (User) customUserDetailsService.loadUserByUsername(username);
		return user;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<CategoryDTO> get(@PathVariable Long companyId, @PathVariable Long id) {

		// Checking logged username, and getting user in order to check privileges -
		// must access only his own company
		// Category must be in user company

		User user = loggedUser();

		Category category = categoryService.findOne(id);

		if (user.getCompany().getId() == companyId && user.getCompany().getCategories().contains(category)) {

			if (category == null) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}

			CategoryDTO dto = toDTO.convert(category);

			return new ResponseEntity<>(dto, HttpStatus.OK);
		} else
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

	}

	@RequestMapping(method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<CategoryDTO> add(@PathVariable Long companyId,
			@RequestBody @Validated CategoryDTO newCategory) {

		User user = loggedUser();

		if (user.getCompany().getId() == companyId) {
			
			newCategory.setCompanyId(user.getCompany().getId());
			newCategory.setCompanyName(user.getCompany().getName());
			
			Category category = toCategory.convert(newCategory);
			
			categoryService.save(category);
			
			CategoryDTO dto = toDTO.convert(category);

			return new ResponseEntity<>(dto, HttpStatus.CREATED);
		} else
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{id}", consumes = "application/json")
	public ResponseEntity<CategoryDTO> edit(@PathVariable Long companyId, @PathVariable Long id,
			@RequestBody @Validated CategoryDTO editedCategory) {

		User user = loggedUser();

		if (user.getCompany().getId() == companyId && approvedCategory(id, companyId)) {

			if (!id.equals(editedCategory.getId())) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
			
			editedCategory.setCompanyId(user.getCompany().getId());
			editedCategory.setCompanyName(user.getCompany().getName());
			
			Category category = toCategory.convert(editedCategory);

			categoryService.save(category);

			CategoryDTO dto = toDTO.convert(category);

			return new ResponseEntity<>(dto, HttpStatus.OK);
		} else
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<CategoryDTO> delete(@PathVariable Long companyId, @PathVariable Long id) {

		User user = loggedUser();

		if (user.getCompany().getId() == companyId && approvedCategory(id, companyId)) {
			categoryService.remove(id);

			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

	}

	// Method to determine if edited or deleted category belongs to company
	private boolean approvedCategory(Long id, Long companyId) {
		Category categoryToEdit = categoryService.findOne(id);
		return categoryToEdit.getCompany().getId()==companyId;
	}

	@ExceptionHandler
	public ResponseEntity<Void> validationHandler(DataIntegrityViolationException e) {

		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

}
