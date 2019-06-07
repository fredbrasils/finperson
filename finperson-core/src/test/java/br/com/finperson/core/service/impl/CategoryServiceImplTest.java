package br.com.finperson.core.service.impl;


import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.finperson.FinPersonTestConfiguration;
import br.com.finperson.core.exception.EntityExistsException;
import br.com.finperson.core.service.CategoryService;
import br.com.finperson.core.service.RoleService;
import br.com.finperson.core.service.UserService;
import br.com.finperson.model.CategoryEntity;
import br.com.finperson.model.RoleEntity;
import br.com.finperson.model.UserEntity;
import br.com.finperson.model.enumm.RoleEnum;
import br.com.finperson.util.ConstantsMessages;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = FinPersonTestConfiguration.class)
@TestInstance(Lifecycle.PER_CLASS)
class CategoryServiceImplTest {

	@Autowired
	RoleService roleService;
	
	@Autowired
	CategoryService categoryService;

	@Autowired
	UserService userService;
	
	UserEntity user1;
	UserEntity user2;
	UserEntity user3;
	
	@BeforeAll
	void setUp() {

		Set<RoleEntity> returnRolesSet = new HashSet<RoleEntity>();
		returnRolesSet.add(roleService.findByRole(RoleEnum.ROLE_USER));		
				
		/* USER 1 */
		user1 = UserEntity.builder().firstName("Fred").enabled(true).nonLocked(false)
				.lastName("Brasil").email("fredbrasils@hotmail.com").roles(returnRolesSet)
				.build();
		
		user1 = userService.save(user1);
		
		CategoryEntity transportCategory = CategoryEntity.builder().name("Transport").color("10-10-10-1").icon("fas fa-plus").user(user1).build();
		CategoryEntity healthCategory = CategoryEntity.builder().name("Health").color("10-10-10-1").icon("fas fa-plus").user(user1).build();
		categoryService.save(transportCategory);
		categoryService.save(healthCategory);

		CategoryEntity medicationCategory = CategoryEntity.builder().name("Medication").color("10-10-10-1").icon("fas fa-plus")
				.user(user1).mainCategory(categoryService.findByNameAndUser("Health", user1).get())
				.build();
		categoryService.save(medicationCategory);

		CategoryEntity homeMedicationCategory = CategoryEntity.builder().name("Home Medication").color("10-10-10-1").icon("fas fa-plus")
				.user(user1).mainCategory(categoryService.findByNameAndUser("Health", user1).get())
				.build();
		categoryService.save(homeMedicationCategory);
		
		/* USER 2 */
		user2 = UserEntity.builder().firstName("Paul").enabled(true).nonLocked(false)
				.lastName("Tremblait").email("paul.trem@hotmail.com").roles(returnRolesSet)
				.build();
		
		user2 = userService.save(user2);

		CategoryEntity sportCategory = CategoryEntity.builder().name("Sport").color("10-10-10-1").icon("fas fa-plus").user(user2).build();
		CategoryEntity foodCategory = CategoryEntity.builder().name("Food").color("10-10-10-1").icon("fas fa-plus").user(user2).build();
		CategoryEntity abcCategory = CategoryEntity.builder().name("Abc").color("10-10-10-1").icon("fas fa-plus").user(user2).build();
		
		categoryService.save(sportCategory);
		categoryService.save(foodCategory);
		categoryService.save(abcCategory);
		
		/* USER 3 */
		user3 = UserEntity.builder().firstName("John").enabled(true).nonLocked(false)
				.lastName("Snow").email("john.snow@hotmail.com").roles(returnRolesSet)
				.build();
		
		user3 = userService.save(user3);

		CategoryEntity homeCategory = CategoryEntity.builder().name("Home").color("10-10-10-1").icon("fas fa-plus").user(user3).build();
		CategoryEntity workCategory = CategoryEntity.builder().name("Work").color("10-10-10-1").icon("fas fa-plus").user(user3).build();		
		
		categoryService.save(homeCategory);
		categoryService.save(workCategory);
	}
	
	@DisplayName("Find all category from user without subcategories")
	@Test
	void findAllByUserWithoutSubcategories() {
		
		List<CategoryEntity> categories = categoryService.findAllByUser(user2);

		assertNotNull(categories);
		
		categories.stream().forEach(cat -> 
				assertTrue(cat.getSubCategories().isEmpty())				
		);
		
	}
	
	@DisplayName("Find all category from user with subcategories")
	@Test
	void findAllByUser() {
		
		List<CategoryEntity> categories = categoryService.findAllByUser(user1);

		assertNotNull(categories);
		
		categories.stream().forEach(cat -> {
			if(cat.getName().equals("Health")) {
				assertEquals(2, cat.getSubCategories().size());				
			}
		});
		
	}

	@DisplayName("Find category from user")
	@Test
	void findById() {
		
		Long id = categoryService.findByNameAndUser("Sport",user2).get().getId();
		CategoryEntity category = categoryService.findByIdAndUser(id,user2).get();
		assertNotNull(category);		
	}		
	
	@DisplayName("Delete category")
	@Test
	void delete() {
		
		CategoryEntity category = categoryService.findByNameAndUser("Home",user3).get();

		categoryService.delete(category);
		
		assertTrue(categoryService.findByNameAndUser("Home",user3).isEmpty());
	}

	@DisplayName("Delete category from id")
	@Test
	void deleteById() {
	
		Long id = categoryService.findByNameAndUser("Work",user3).get().getId();		
		categoryService.deleteById(id);		
		assertTrue(categoryService.findByIdAndUser(id,user3).isEmpty());
	}
	
	
	@DisplayName("Create the same category to another user")
	@Test
	void createTheSameCategoryToAnotherUser() {
		
		CategoryEntity category = CategoryEntity.builder().name("Sport").color("10-10-10-1").icon("fas fa-plus").user(user1).build();

		try {
			category = categoryService.create(category);
		} catch (EntityExistsException e) {
		}

		assertNotNull(category.getId());
		
	}
		
	@DisplayName("Don't create because category exists")
	@Test
	void dontCreateCategoryExists() {
		
		CategoryEntity category = CategoryEntity.builder().name("transport").color("10-10-10-1").icon("fas fa-plus").user(user1).build();

		try {
			category = categoryService.create(category);
			fail();
		} catch (EntityExistsException e) {
			assertEquals(ConstantsMessages.CATEGORY_MESSAGE_ERROR_EXISTS, e.getMessage());
		}

	}
	
	@DisplayName("Update category from user")
	@Test
	void update() {		
				
		CategoryEntity category = categoryService.findByNameAndUser("Food", user2).get();
		category.setName("Food home");
		
		try {
			category = categoryService.update(category);
		} catch (EntityExistsException e) {
			fail();
		}

		category = categoryService.findByNameAndUser("Food home", user2).get();
		assertEquals("Food home", category.getName());
	}
	
	@DisplayName("Don't Update category from user")
	@Test
	void dontUpdate() {		
				
		CategoryEntity category = categoryService.findByNameAndUser("Abc", user2).get();
		category.setName("Sport");
		
		try {
			category = categoryService.update(category);
			fail();
		} catch (EntityExistsException e) {
			assertEquals(ConstantsMessages.CATEGORY_MESSAGE_ERROR_EXISTS, e.getMessage());
		}

		category = categoryService.findByNameAndUser("Abc", user2).get();
		assertEquals("Abc", category.getName());
	}
	
	@DisplayName("Update category with the same name from user")
	@Test
	void updateTheSameCategory() {		
				
		CategoryEntity category = categoryService.findByNameAndUser("Transport", user1).get();
		category.setName("Transport");
		
		try {
			category = categoryService.update(category);
		} catch (EntityExistsException e) {
			fail();
		}

		category = categoryService.findByNameAndUser("Transport", user1).get();
		assertEquals("Transport", category.getName());
	}
	
	@DisplayName("Find category by name and user")
	@Test
	void findByNameAndUser() {
		
		Optional<CategoryEntity> category = categoryService.findByNameAndUser("Sport",user2);		
		assertNotNull(category.get());		
		assertEquals("Sport", category.get().getName());
	}
	
	/* SUBCATEGORY */
	
	@DisplayName("Create subcategory")
	@Test
	void createSubCategory() {
		
		CategoryEntity category = CategoryEntity.builder().name("Uber").color("10-10-10-1")
				.icon("fas fa-plus").user(user1)
				.mainCategory(categoryService.findByNameAndUser("Transport", user1).get())
				.build();

		try {
			category = categoryService.createSubCategory(category);
		} catch (EntityExistsException e) {
			fail();
		}

		assertNotNull(category.getId());
		assertNotNull(category.getMainCategory().getId());
	}
	
	@DisplayName("Dont create a subcategory that already exist ")
	@Test
	void dontCreateSubCategory() {
		
		CategoryEntity category = CategoryEntity.builder().name("Medication").color("10-10-10-1").icon("fas fa-plus")
				.user(user1).mainCategory(categoryService.findByNameAndUser("Health", user1).get())
				.build();				
		
		try {
			category = categoryService.createSubCategory(category);
			fail();
		} catch (EntityExistsException e) {
			assertEquals(ConstantsMessages.CATEGORY_MESSAGE_ERROR_EXISTS, e.getMessage());
		}

		assertNull(category.getId());
	}
}
