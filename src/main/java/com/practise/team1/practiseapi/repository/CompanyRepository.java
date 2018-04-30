package com.practise.team1.practiseapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.practise.team1.practiseapi.model.Company;
@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

}
