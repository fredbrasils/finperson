package br.com.finperson.core.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.finperson.core.repository.RoleRepository;
import br.com.finperson.domain.RoleEntity;
import br.com.finperson.domain.enumm.RoleEnum;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

	@Mock
	RoleRepository roleRepository;

	@InjectMocks
	RoleServiceImpl roleService;

	RoleEntity returnRole;

	Set<RoleEntity> returnRolesSet;
	
	@BeforeEach
	void setUp() {
		
		returnRolesSet = new HashSet<RoleEntity>();
		returnRolesSet.add(RoleEntity.builder().id(1l).role(RoleEnum.ROLE_ADIM).build());
		returnRolesSet.add(RoleEntity.builder().id(2l).role(RoleEnum.ROLE_USER).build());
		returnRolesSet.add(RoleEntity.builder().id(2l).role(RoleEnum.ROLE_GUEST).build());
		
		returnRole = RoleEntity.builder().id(1l)
				.role(RoleEnum.ROLE_ADIM)
				.build();
	}

	@Test
	void findAll() {
		
		Set<RoleEntity> returnRolesSet = new HashSet<RoleEntity>();
		returnRolesSet.add(RoleEntity.builder().id(1l).build());
		returnRolesSet.add(RoleEntity.builder().id(2l).build());

		when(roleRepository.findAll()).thenReturn(returnRolesSet);

		Set<RoleEntity> roles = roleService.findAll();

		assertNotNull(roles);
		assertEquals(2, roles.size());
	}

	@Test
	void findById() {
		when(roleRepository.findById(anyLong())).thenReturn(Optional.of(returnRole));

		RoleEntity role = roleService.findById(1L);

		assertNotNull(role);
	}

	@Test
	void findByIdNotFound() {
		when(roleRepository.findById(anyLong())).thenReturn(Optional.empty());

		RoleEntity role = roleService.findById(1L);

		assertNull(role);
	}

	@Test
	void save() {
		RoleEntity roleToSave = RoleEntity.builder().id(1L).build();

		when(roleRepository.save(any())).thenReturn(returnRole);

		RoleEntity savedRole = roleService.save(roleToSave);

		assertNotNull(savedRole);

		verify(roleRepository).save(any());
	}

	@Test
	void delete() {
		roleService.delete(returnRole);

		// default is 1 times
		verify(roleRepository, times(1)).delete(any());
	}

	@Test
	void deleteById() {
		roleService.deleteById(1L);

		verify(roleRepository).deleteById(anyLong());
	}
	
	@Test
	void findByRole() {
		
		when(roleRepository.findByRole(any())).thenReturn(returnRole);

		RoleEntity findedRole = roleService.findByRole(RoleEnum.ROLE_ADIM);

		assertNotNull(findedRole);

		verify(roleRepository).findByRole(any());

	}
}
