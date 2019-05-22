package br.com.finperson.core.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.finperson.FinPersonTestConfiguration;
import br.com.finperson.core.service.RoleService;
import br.com.finperson.model.RoleEntity;
import br.com.finperson.model.enumm.RoleEnum;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = FinPersonTestConfiguration.class)
class RoleServiceImplTest {

	@Autowired
	RoleService roleService;

	@DisplayName("Find all roles")
	@Test
	void findAll() {
		Set<RoleEntity> roles = roleService.findAll();
		assertNotNull(roles);
		assertEquals(3, roles.size());
	}

	@DisplayName("Find role by id")
	@Test
	void findById() {
		RoleEntity role = roleService.findById(1L);
		assertNotNull(role);
	}

	@DisplayName("Not Find role by id")
	@Test
	void findByIdNotFound() {
		RoleEntity role = roleService.findById(999L);
		assertNull(role);
	}	
		
	@DisplayName("Find role by role's name")
	@Test
	void findByRole() {
		RoleEntity findedRole = roleService.findByRole(RoleEnum.ROLE_ADMIN);
		assertNotNull(findedRole);
	}
}
