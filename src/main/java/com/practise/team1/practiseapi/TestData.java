package com.practise.team1.practiseapi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.practise.team1.practiseapi.model.Category;
import com.practise.team1.practiseapi.model.Company;
import com.practise.team1.practiseapi.model.InventoryItem;
import com.practise.team1.practiseapi.model.Role;
import com.practise.team1.practiseapi.model.User;
import com.practise.team1.practiseapi.repository.RoleRepository;
import com.practise.team1.practiseapi.service.CategoryService;
import com.practise.team1.practiseapi.service.CompanyService;
import com.practise.team1.practiseapi.service.InventoryItemService;
import com.practise.team1.practiseapi.service.UserService;

@Component
public class TestData {

	@Autowired
	private CompanyService companyService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private InventoryItemService itemService;

	@Autowired
	private UserService userService;

	@Autowired
	private RoleRepository roleRepository;

	@PostConstruct
	public void init() {

		Date date = null;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		for (int i = 1; i < 4; i++) {
			Company company = new Company();
			company.setName("Company " + i);
			company.setAddress("Address " + i);
			company.setContactPerson("Contact preson " + i);
			try {
				date = sdf.parse("10/10/202" + i);
			} catch (ParseException e) {
				System.out.println("Exception occured during date parsing");
				e.printStackTrace();
			}
			company.setValidLicenceTill(date);
			companyService.save(company);

			for (int j = 1; j < 4; j++) {
				Category category = new Category();
				category.setName("Category " + j + " of company " + i);
				category.setCompany(company);
				company.addCategory(category);
				categoryService.save(category);

				for (int k = 1; k < 6; k++) {
					InventoryItem item = new InventoryItem();
					item.setName("Item " + k + " of category " + j + " of company " + i);
					item.setDescription("Item " + k + " described here.");
					item.setOrderNumber((int) (i * 100 + j + 10 + k));
					item.setValue((float) (150 + i + j + k));
					item.setBarcode((int) (i * 100 + j + 10 + k));
					item.setCategory(category);
					category.addInventoryItem(item);
					itemService.save(item);
				}
			}
		}

		/* Super admin ------------------------- */
		User user1 = new User();
		user1.setFirstName("Super administrator");
		user1.setLastName("SuperLast");
		user1.setEmail("super@test.com");
		user1.setPassword("super");

		Role role1 = new Role();
		role1.setRole("SUPER_ADMIN");

		user1.addRole(role1);
		role1.setUser(user1);

		userService.save(user1);
		roleRepository.save(role1);
		/* ------------------------------------- */

		/* Company admin ------------------------- */
		User user2 = new User();
		user2.setFirstName("Company administrator");
		user2.setLastName("CompanyLast");
		// To check password recovery feature, enter your valid email to the password,
		// in this case "company"
		user2.setEmail("youremail@comething.com");
		user2.setPassword("company");

		Role role2 = new Role();
		role2.setRole("COMPANY_ADMIN");

		user2.addRole(role2);
		role2.setUser(user2);

		Company company = companyService.findOne(1L);
		user2.setCompany(company);
		company.addUser(user2);

		userService.save(user2);
		roleRepository.save(role2);
		companyService.save(company);
		/* ------------------------------------- */
	}

}
