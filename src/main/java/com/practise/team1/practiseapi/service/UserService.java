package com.practise.team1.practiseapi.service;

import org.springframework.data.domain.Page;
import com.practise.team1.practiseapi.model.User;

public interface UserService {

	Page<User> findAll(int pageNum);

	User findOne(Long id);

	void save(User user);

	void remove(Long id);

	Page<User> findByCompanyId(int pageNum, Long companyId);
}
