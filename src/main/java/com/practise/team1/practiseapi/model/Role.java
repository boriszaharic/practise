package com.practise.team1.practiseapi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column
	private Long id;
	@Column
	private String role;
	@ManyToOne(fetch = FetchType.EAGER)
	User user;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		if ("SUPER_ADMIN".equals(role)) {
			this.role = role;
		}
		else if ("COMPANY_ADMIN".equals(role)) {
			this.role = role;
		} else
			this.role = "USER";

	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
		if (user != null && !user.getRoles().contains(this)) {
			user.getRoles().add(this);
		}
	}

	@Override
	public String toString() {
		return "Role [role=" + role + "]";
	}

}
