package com.practise.team1.practiseapi.support;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.practise.team1.practiseapi.model.Role;
import com.practise.team1.practiseapi.model.User;
import com.practise.team1.practiseapi.service.CompanyService;
import com.practise.team1.practiseapi.service.UserService;
import com.practise.team1.practiseapi.web.dto.UserModifyDTO;

@Component
public class UserModifyDTOToUser implements Converter<UserModifyDTO, User> {

	@Autowired
	UserService userService;

	@Autowired
	CompanyService companyService;

	@Override
	public User convert(UserModifyDTO dto) {

		User user = new User();

		if (dto.getId() != null) {
			user = userService.findOne(dto.getId());

			if (user == null) {
				throw new IllegalStateException("Tried to modify a non-existant user");
			}

		}

		user.setId(dto.getId());
		user.setFirstName(dto.getFirstname());
		user.setLastName(dto.getLastname());
		user.setEmail(dto.getEmail());

		if (dto.getPassword().equals(dto.getPasswordConfirm())) {
			user.setPassword(dto.getPassword());
		} else
			throw new IllegalStateException("Password not confirmed correctly!");

		Role role = new Role();
		role.setRole(dto.getRole());
		user.addRole(role);

		user.setCompany(companyService.findOne(dto.getCompanyId()));

		return user;
	}

	public List<User> convert(List<UserModifyDTO> dtos) {
		List<User> users = new ArrayList<>();

		for (UserModifyDTO dto : dtos) {
			users.add(convert(dto));
		}

		return users;
	}

}
