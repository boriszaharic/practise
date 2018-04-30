package com.practise.team1.practiseapi.web.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.web.servlet.ModelAndView;

import com.practise.team1.practiseapi.model.Category;
import com.practise.team1.practiseapi.model.Company;
import com.practise.team1.practiseapi.model.InventoryItem;
import com.practise.team1.practiseapi.model.User;
import com.practise.team1.practiseapi.service.CategoryService;
import com.practise.team1.practiseapi.service.InventoryItemService;
import com.practise.team1.practiseapi.service.impl.CustomUserDetailsService;
import com.practise.team1.practiseapi.support.InventoryItemDTOToInventoryItem;
import com.practise.team1.practiseapi.support.InventoryItemToInventoryItemDTO;
import com.practise.team1.practiseapi.view.MyExcelView;
import com.practise.team1.practiseapi.web.dto.InventoryItemDTO;

@RestController
@RequestMapping(value = "/company/{companyId}/categories/{categoryId}/items")
@PreAuthorize("hasAnyRole('COMPANY_ADMIN')")
public class ApiInventoryItemController {
	@Autowired
	private InventoryItemService inventoryItemService;

	@Autowired
	private InventoryItemToInventoryItemDTO toDTO;

	@Autowired
	private InventoryItemDTOToInventoryItem toInventoryItem;

	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	@Autowired
	private CategoryService categoryService;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<InventoryItemDTO>> get(@PathVariable Long companyId, @PathVariable Long categoryId,
			@RequestParam(defaultValue = "0") int pageNum) {

		// method for getting logged user

		User user = loggedUser();

		Company company = user.getCompany();
		Category category = categoryService.findOne(categoryId);

		// user must access only his own company, and category must be inside company
		if (company.getId() == companyId && company.getCategories().contains(category)) {

			Page<InventoryItem> inventoryItems = inventoryItemService.findByCategoryId(pageNum, categoryId);

			HttpHeaders headers = new HttpHeaders();
			headers.add("totalPages", Integer.toString(inventoryItems.getTotalPages()));

			List<InventoryItemDTO> dtos = toDTO.convert(inventoryItems.getContent());

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

	// method to download all items of category
	@RequestMapping(method = RequestMethod.GET, value = "/download")
	public ModelAndView download(HttpServletRequest request, HttpServletResponse response, @PathVariable Long companyId,
			@PathVariable Long categoryId) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd MM yyyy");
		String printDate = sdf.format(date);
		Map<String, Object> model = new HashMap<String, Object>();
		// Sheet Name
		model.put("sheetname", "Inventory items on " + printDate);
		// Headers List
		List<String> headers = new ArrayList<String>();
		headers.add("ID");
		headers.add("NAME");
		headers.add("ORDER NUMBER");
		headers.add("VALUE");
		headers.add("DESCRIPTION");
		headers.add("BARCODE");
		model.put("headers", headers);
		// Results Table (List<Object[]>)
		List<List<String>> results = new ArrayList<List<String>>();

		// method for getting logged user
		User user = loggedUser();

		Company company = user.getCompany();
		Category category = categoryService.findOne(categoryId);

		// user must access only his own company, and category must be inside company
		if (company.getId() == companyId && company.getCategories().contains(category)) {

			List<InventoryItem> inventoryItems = inventoryItemService.findByCategoryId(categoryId);

			List<InventoryItemDTO> dtos = toDTO.convert(inventoryItems);

			for (InventoryItemDTO inventoryItemDTO : dtos) {
				results.add(inventoryItemDTO.toStringAll());
			}

			model.put("results", results);
			response.setContentType("application/ms-excel");
			response.setHeader("Content-disposition", "attachment; filename=exported_items.xls");
			return new ModelAndView(new MyExcelView(), model);
		}
		return null;

	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<InventoryItemDTO> get(@PathVariable Long companyId, @PathVariable Long categoryId,
			@PathVariable Long id) {

		// Checking logged username, and getting user in order to check privileges -
		// must access only his own company
		// InventoryItem must be in user company

		User user = loggedUser();

		Company company = user.getCompany();
		Category category = categoryService.findOne(categoryId);

		InventoryItem inventoryItem = inventoryItemService.findOne(id);

		if (company.getId() == companyId && company.getCategories().contains(category)
				&& category.getInventoryItems().contains(inventoryItem)) {

			InventoryItemDTO dto = toDTO.convert(inventoryItem);

			return new ResponseEntity<>(dto, HttpStatus.OK);
		} else
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);

	}

	@RequestMapping(method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<InventoryItemDTO> add(@PathVariable Long companyId, @PathVariable Long categoryId,
			@RequestBody @Validated InventoryItemDTO newInventoryItem) {

		User user = loggedUser();
		Company company = user.getCompany();
		Category category = categoryService.findOne(categoryId);

		if (company.getId() == companyId && company.getCategories().contains(category)) {

			newInventoryItem.setCategoryId(categoryId);
			newInventoryItem.setCategoryName(category.getName());

			InventoryItem inventoryItem = toInventoryItem.convert(newInventoryItem);

			inventoryItemService.save(inventoryItem);

			InventoryItemDTO dto = toDTO.convert(inventoryItem);

			return new ResponseEntity<>(dto, HttpStatus.CREATED);
		} else
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{id}", consumes = "application/json")
	public ResponseEntity<InventoryItemDTO> edit(@PathVariable Long companyId, @PathVariable Long categoryId,
			@PathVariable Long id, @RequestBody @Validated InventoryItemDTO editedInventoryItem) {

		User user = loggedUser();
		Company company = user.getCompany();
		// Category category = categoryService.findOne(categoryId);

		if (company.getId() == companyId && approvedInventoryItem(id, companyId, categoryId)) {

			if (!id.equals(editedInventoryItem.getId())) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}

			editedInventoryItem.setCategoryId(categoryId);
			editedInventoryItem.setCategoryName(categoryService.findOne(categoryId).getName());

			InventoryItem inventoryItem = toInventoryItem.convert(editedInventoryItem);

			inventoryItemService.save(inventoryItem);

			InventoryItemDTO dto = toDTO.convert(inventoryItem);

			return new ResponseEntity<>(dto, HttpStatus.OK);
		} else
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<InventoryItemDTO> delete(@PathVariable Long companyId, @PathVariable Long categoryId,
			@PathVariable Long id) {

		User user = loggedUser();
		Company company = user.getCompany();
		// Category category = categoryService.findOne(categoryId);

		if (company.getId() == companyId && approvedInventoryItem(id, companyId, categoryId)) {
			inventoryItemService.remove(id);

			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

	}

	/*
	 * I had big problem to get null if service could not find entity by ID, so this
	 * additional complexed code is result of that. Until I used existsById(id)
	 * method in jpa repository to get null when there is no entity with given id.
	 * No time to refactor now :)
	 */
	// Method to determine if edited or deleted inventoryItem belongs to category
	private boolean approvedInventoryItem(Long id, Long companyId, Long categoryId) {
		// Category category = categoryService.findOne(categoryId);
		InventoryItem inventoryItemToEdit = inventoryItemService.findOne(id);
		if (inventoryItemToEdit == null)
			return false;
		return inventoryItemToEdit.getCategory().getCompany().getId() == companyId
				&& inventoryItemToEdit.getCategory().getId() == categoryId;
	}

	@ExceptionHandler
	public ResponseEntity<Void> validationHandler(DataIntegrityViolationException e) {

		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

}
