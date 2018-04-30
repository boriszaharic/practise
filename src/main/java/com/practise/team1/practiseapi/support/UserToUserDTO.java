package com.practise.team1.practiseapi.support;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.practise.team1.practiseapi.model.User;
import com.practise.team1.practiseapi.web.dto.UserDTO;

@Component
public class UserToUserDTO implements Converter<User, UserDTO> {

	@Override
	public UserDTO convert(User user) {

		UserDTO dto = new UserDTO();

		dto.setId(user.getId());

		dto.setFirstName(user.getFirstName());

		dto.setLastName(user.getLastName());

		dto.setEmail(user.getEmail());
		if (user.getCompany() != null) {
			dto.setCompanyId(user.getCompany().getId());

			dto.setCompanyName(user.getCompany().getName());
		}else {
			dto.setCompanyId(0L);
			dto.setCompanyName("SUPER_ADMIN");
		}

		return dto;
	}

	public List<UserDTO> convert(List<User> users) {

		List<UserDTO> dtos = new ArrayList<>();

		for (User user : users) {
			dtos.add(convert(user));
		}

		return dtos;

	}

}
