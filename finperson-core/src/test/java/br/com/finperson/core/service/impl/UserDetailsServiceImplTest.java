package br.com.finperson.core.service.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;

import br.com.finperson.core.repository.UserRepository;
import br.com.finperson.core.service.impl.UserDetailsServiceImpl;
import br.com.finperson.model.RoleEntity;
import br.com.finperson.model.UserEntity;
import br.com.finperson.model.enumm.RoleEnum;

@ContextConfiguration
@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

	@Mock
	UserRepository userRepository;

	@InjectMocks
	UserDetailsServiceImpl userDetailsService;

	UserEntity returnUser;

	@BeforeEach
	void setUp() {

		Set<RoleEntity> returnRolesSet = new HashSet<RoleEntity>();
		returnRolesSet.add(RoleEntity.builder().id(1l).role(RoleEnum.ROLE_USER).build());

		returnUser = UserEntity.builder().id(1l)
				.firstName("Fred")
				.lastName("Brasil")
				.email("fredbrasils@hotmail.com")
				.password("1234")
				.enabled(true).nonLocked(true)
				.roles(returnRolesSet).build();
		
	}

	@DisplayName(value="Return a user of the system.")
	@Test
	void loadUserByUsername() {

		when(userRepository.findByEmail(any())).thenReturn(returnUser);

		UserDetails userSecurity = userDetailsService.loadUserByUsername("user");

		assertNotNull(userSecurity);
	}

	@DisplayName(value="Throws exception when try return a user that don't exist")
	@Test
	void nonLoadUserByUsername() {

		assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("user"));
	}
}
