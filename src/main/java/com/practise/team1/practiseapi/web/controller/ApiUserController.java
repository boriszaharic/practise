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

import com.practise.team1.practiseapi.model.User;
import com.practise.team1.practiseapi.service.UserService;
import com.practise.team1.practiseapi.service.impl.CustomUserDetailsService;
import com.practise.team1.practiseapi.support.UserModifyDTOToUser;
import com.practise.team1.practiseapi.support.UserToUserDTO;
import com.practise.team1.practiseapi.support.UserToUserModifyDTO;
import com.practise.team1.practiseapi.web.dto.UserDTO;
import com.practise.team1.practiseapi.web.dto.UserModifyDTO;

@RestController
@RequestMapping(value = "/users")
public class ApiUserController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserToUserDTO toUserDTO;

	@Autowired
	private UserToUserModifyDTO toDTO;

	@Autowired
	private UserModifyDTOToUser toUser;

	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	@PreAuthorize("hasAnyRole('SUPER_ADMIN')")
	@RequestMapping(method = RequestMethod.GET, value = "/all")
	public ResponseEntity<List<UserDTO>> get(@RequestParam(defaultValue = "0") int pageNum) {

		Page<User> users = userService.findAll(pageNum);

		HttpHeaders headers = new HttpHeaders();
		headers.add("totalPages", Integer.toString(users.getTotalPages()));

		List<UserDTO> dtos = toUserDTO.convert(users.getContent());

		return new ResponseEntity<>(dtos, headers, HttpStatus.OK);

	}

	@PreAuthorize("hasAnyRole('COMPANY_ADMIN')")
	@RequestMapping(method = RequestMethod.GET, value = "/company/{companyId}")
	public ResponseEntity<List<UserModifyDTO>> get(@PathVariable Long companyId,
			@RequestParam(defaultValue = "0") int pageNum) {

		// method for getting logged user

		User user = loggedUser();

		// user must access only his own company
		if (user.getCompany().getId() == companyId) {

			Page<User> users = userService.findByCompanyId(pageNum, companyId);

			HttpHeaders headers = new HttpHeaders();
			headers.add("totalPages", Integer.toString(users.getTotalPages()));

			List<UserModifyDTO> dtos = toDTO.convert(users.getContent());

			return new ResponseEntity<>(dtos, headers, HttpStatus.OK);
		} else
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

	}

	// Checking logged user in order to check access privileges
	private User loggedUser() {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		String username = auth.getName();

		User user = (User) customUserDetailsService.loadUserByUsername(username);
		return user;
	}

	@PreAuthorize("hasAnyRole('COMPANY_ADMIN')")
	@RequestMapping(method = RequestMethod.GET, value = "/company/{companyId}/user/{id}")
	public ResponseEntity<UserModifyDTO> get(@PathVariable Long companyId, @PathVariable Long id) {

		// Checking logged username, and getting user in order to check privileges -
		// must access only his own company
		// User to find must be in same user company

		User user = loggedUser();

		User userToFind = userService.findOne(id);

		if (user.getCompany().getId() == companyId && user.getCompany().getUsers().contains(userToFind)) {

			if (userToFind == null) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}

			UserModifyDTO dto = toDTO.convert(userToFind);

			return new ResponseEntity<>(dto, HttpStatus.OK);
		} else
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

	}

	@PreAuthorize("hasAnyRole('COMPANY_ADMIN')")
	@RequestMapping(method = RequestMethod.POST, consumes = "application/json", value = "/company/{companyId}")
	public ResponseEntity<UserModifyDTO> add(@PathVariable Long companyId,
			@RequestBody @Validated UserModifyDTO newUser) {

		User user = loggedUser();

		// checking if user have authorization to make new user,
		// and that he can not create SUPER_ADMIN
		if (user.getCompany().getId() == companyId && !"SUPER_ADMIN".equals(newUser.getRole())) {

			newUser.setCompanyId(user.getCompany().getId());
			newUser.setCompanyName(user.getCompany().getName());

			User newUserSaved = toUser.convert(newUser);

			userService.save(newUserSaved);

			UserModifyDTO dto = toDTO.convert(newUserSaved);

			return new ResponseEntity<>(dto, HttpStatus.CREATED);
		} else
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

	}

	@PreAuthorize("hasAnyRole('COMPANY_ADMIN')")
	@RequestMapping(method = RequestMethod.PUT, value = "/company/{companyId}/user/{id}", consumes = "application/json")
	public ResponseEntity<UserModifyDTO> edit(@PathVariable Long companyId, @PathVariable Long id,
			@RequestBody @Validated UserModifyDTO editedUser) {

		User user = loggedUser();

		if (user.getCompany().getId() == companyId && approvedUser(id, companyId)
				&& !"SUPER_ADMIN".equals(editedUser.getRole())) {

			if (!id.equals(editedUser.getId())) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}

			editedUser.setCompanyId(user.getCompany().getId());
			editedUser.setCompanyName(user.getCompany().getName());

			User userToSave = toUser.convert(editedUser);

			userService.save(userToSave);

			UserModifyDTO dto = toDTO.convert(userToSave);

			return new ResponseEntity<>(dto, HttpStatus.OK);
		} else
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

	}

	@PreAuthorize("hasAnyRole('COMPANY_ADMIN')")
	@RequestMapping(method = RequestMethod.DELETE, value = "/company/{companyId}/user/{id}")
	public ResponseEntity<UserModifyDTO> delete(@PathVariable Long companyId, @PathVariable Long id) {

		User user = loggedUser();

		if (user.getCompany().getId() == companyId && approvedUser(id, companyId)) {
			userService.remove(id);

			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

	}

	// Method to determine if edited or deleted user belongs to company
	private boolean approvedUser(Long id, Long companyId) {
		User userToEdit = userService.findOne(id);
		return userToEdit.getCompany().getId() == companyId;
	}

	@ExceptionHandler
	public ResponseEntity<Void> validationHandler(DataIntegrityViolationException e) {

		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
}
