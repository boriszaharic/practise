package com.practise.team1.practiseapi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.practise.team1.practiseapi.model.Category;
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

	Page<Category> findByCompanyId(Pageable pageRequest, Long companyId);

}
