package br.com.finperson.core.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.finperson.core.exception.EmailExistsException;
import br.com.finperson.core.repository.RoleRepository;
import br.com.finperson.core.repository.UserRepository;
import br.com.finperson.domain.RoleEntity;
import br.com.finperson.domain.UserEntity;
import br.com.finperson.domain.enumm.RoleEnum;
import br.com.finperson.security.domain.UserDTO;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

	@Mock
	UserRepository userRepository;

	@Mock
	RoleRepository roleRepository;
	
	@Mock
	PasswordEncoder passwordEncoder;
	
	@InjectMocks
	UserServiceImpl userService;

	UserEntity returnUser;

	Set<RoleEntity> returnRolesSet;
	
	@BeforeEach
	void setUp() {
		
		returnRolesSet = new HashSet<RoleEntity>();
		returnRolesSet.add(RoleEntity.builder().id(1l).role(RoleEnum.ROLE_ADIM).build());
		returnRolesSet.add(RoleEntity.builder().id(2l).role(RoleEnum.ROLE_USER).build());
		returnRolesSet.add(RoleEntity.builder().id(2l).role(RoleEnum.ROLE_GUEST).build());
		
		returnUser = UserEntity.builder().id(1l)
				.firstName("Fred")
				.lastName("Brasil")
				.email("fredbrasils@hotmail.com")
				.roles(returnRolesSet)
				.build();
	}

	@Test
	void testFindAll() {
		
		Set<UserEntity> returnUsersSet = new HashSet<UserEntity>();
		returnUsersSet.add(UserEntity.builder().id(1l).build());
		returnUsersSet.add(UserEntity.builder().id(2l).build());

		when(userRepository.findAll()).thenReturn(returnUsersSet);

		Set<UserEntity> users = userService.findAll();

		assertNotNull(users);
		assertEquals(2, users.size());
	}

	@Test
	void testFindById() {
		when(userRepository.findById(anyLong())).thenReturn(Optional.of(returnUser));

		UserEntity user = userService.findById(1L);

		assertNotNull(user);
	}

	@Test
	void testFindByIdNotFound() {
		when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

		UserEntity user = userService.findById(1L);

		assertNull(user);
	}

	@Test
	void testSave() {
		UserEntity userToSave = UserEntity.builder().id(1L).build();

		when(userRepository.save(any())).thenReturn(returnUser);

		UserEntity savedUser = userService.save(userToSave);

		assertNotNull(savedUser);

		verify(userRepository).save(any());
	}

	@Test
	void testDelete() {
		userService.delete(returnUser);

		// default is 1 times
		verify(userRepository, times(1)).delete(any());
	}

	@Test
	void testDeleteById() {
		userService.deleteById(1L);

		verify(userRepository).deleteById(anyLong());
	}
	
	@Test
	void testFindByEmail() {
		
		when(userRepository.findByEmail(any())).thenReturn(returnUser);

		UserEntity findedUser = userService.findByEmail("xxx@hotmail.com");

		assertNotNull(findedUser);

		verify(userRepository).findByEmail(any());

	}
	
	@Test
	void testRegisterNewUserAccount() {
		
		when(userRepository.findByEmail(any())).thenReturn(null);
		when(roleRepository.findByRole(any())).thenReturn(RoleEntity.builder().id(1l).role(RoleEnum.ROLE_ADIM).build());
		when(userRepository.save(any())).thenReturn(returnUser);
		when(passwordEncoder.encode(any())).thenReturn("1234");
		
		UserEntity newUser = null;
		try {
			newUser = userService.registerNewUserAccount(new UserDTO());
		} catch (Exception e) {
			fail();
		}

		assertNotNull(newUser);

		verify(userRepository).save(any());

	}
	
	@Test
	void testNotRegisterUserAccount() {
		
		when(userRepository.findByEmail(any())).thenReturn(returnUser);
		assertThrows(EmailExistsException.class, () -> userService.registerNewUserAccount(new UserDTO()));

	}
}
