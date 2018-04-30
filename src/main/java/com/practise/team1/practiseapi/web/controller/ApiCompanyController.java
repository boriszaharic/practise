package com.practise.team1.practiseapi.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.practise.team1.practiseapi.model.Company;
import com.practise.team1.practiseapi.model.Role;
import com.practise.team1.practiseapi.model.User;
import com.practise.team1.practiseapi.repository.RoleRepository;
import com.practise.team1.practiseapi.service.CompanyService;
import com.practise.team1.practiseapi.service.UserService;
import com.practise.team1.practiseapi.support.CompanyDTOToCompany;
import com.practise.team1.practiseapi.support.CompanyToCompanyDTO;
import com.practise.team1.practiseapi.web.dto.CompanyDTO;

@RestController
@RequestMapping(value = "/companies")
@PreAuthorize("hasAnyRole('SUPER_ADMIN')")
public class ApiCompanyController {

	@Autowired
	private CompanyService companyService;

	@Autowired
	private CompanyToCompanyDTO toDTO;

	@Autowired
	private CompanyDTOToCompany toCompany;

	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleRepository roleRepository;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<CompanyDTO>> get(@RequestParam(defaultValue = "0") int pageNum) {

		Page<Company> companies = companyService.findAll(pageNum);

		HttpHeaders headers = new HttpHeaders();
		headers.add("totalPages", Integer.toString(companies.getTotalPages()));

		List<CompanyDTO> dtos = toDTO.convert(companies.getContent());

		return new ResponseEntity<>(dtos, headers, HttpStatus.OK);

	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<CompanyDTO> get(@PathVariable Long id) {

		Company company = companyService.findOne(id);

		if (company == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		CompanyDTO dto = toDTO.convert(company);

		return new ResponseEntity<>(dto, HttpStatus.OK);

	}

	@RequestMapping(method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<CompanyDTO> add(@RequestBody @Validated CompanyDTO newCompany) {

		// When creating new company, we create one user with company adminstration
		// rights, with username(email in this case) and password as company name
		Company company = toCompany.convert(newCompany);
		
		User user = new User();
		user.setFirstName("Company administrator");
		user.setEmail(newCompany.getName()+"@test.com");
		user.setPassword(newCompany.getName());
		
		Role role = new Role();
		role.setRole("COMPANY_ADMIN");
		
		user.addRole(role);
		role.setUser(user);
		
		user.setCompany(company);
		company.addUser(user);
		
		companyService.save(company);
		userService.save(user);
		roleRepository.save(role);

		CompanyDTO dto = toDTO.convert(company);

		return new ResponseEntity<>(dto, HttpStatus.CREATED);

	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{id}", consumes = "application/json")
	public ResponseEntity<CompanyDTO> edit(@PathVariable Long id, @RequestBody @Validated CompanyDTO editedCompany) {

		if (!id.equals(editedCompany.getId())) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		Company company = toCompany.convert(editedCompany);

		companyService.save(company);

		CompanyDTO dto = toDTO.convert(company);

		return new ResponseEntity<>(dto, HttpStatus.OK);

	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<CompanyDTO> delete(@PathVariable Long id) {

		companyService.remove(id);

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);

	}

	@ExceptionHandler
	public ResponseEntity<Void> validationHandler(DataIntegrityViolationException e) {

		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

}
