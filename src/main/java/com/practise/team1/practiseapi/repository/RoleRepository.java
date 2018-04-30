package com.practise.team1.practiseapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.practise.team1.practiseapi.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

}
