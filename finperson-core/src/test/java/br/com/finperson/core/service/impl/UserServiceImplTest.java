package br.com.finperson.core.service.impl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.finperson.FinPersonTestConfiguration;
import br.com.finperson.core.exception.EmailExistsException;
import br.com.finperson.core.service.RoleService;
import br.com.finperson.core.service.UserService;
import br.com.finperson.model.RoleEntity;
import br.com.finperson.model.TokenEntity;
import br.com.finperson.model.UserEntity;
import br.com.finperson.model.enumm.RoleEnum;
import br.com.finperson.model.enumm.TypeEmailEnum;
import br.com.finperson.model.payload.AuthSingUp;
import br.com.finperson.util.ConstantsMessages;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = FinPersonTestConfiguration.class)
@TestInstance(Lifecycle.PER_CLASS)
class UserServiceImplTest {

	@Autowired
	RoleService roleService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	PasswordEncoder passwordEncoder;

	UserEntity user1;
	UserEntity user2;
	
	@BeforeAll
	void setUp() {
		
		Set<RoleEntity> returnRolesSet = new HashSet<RoleEntity>();
		returnRolesSet.add(roleService.findByRole(RoleEnum.ROLE_USER));
				
		/* USER 1 */
		user1 = UserEntity.builder().firstName("Fred").password("987654").enabled(true).nonLocked(false)
				.lastName("Brasil").email("frederico@hotmail.com").roles(returnRolesSet)
				.build();
		
		user1 = userService.save(user1);
		
		/* USER 2 */
		user2 = UserEntity.builder().firstName("Paul").enabled(false).nonLocked(false)
				.lastName("Tremblait").email("paul@hotmail.com").roles(returnRolesSet)
				.build();
		
		user2 = userService.save(user2);
	}
	
	@DisplayName("Find user by email")
	@Test
	void findUserByEmail() {
		UserEntity findedUser = userService.findByEmail("frederico@hotmail.com");
		assertNotNull(findedUser);
	}
	
	@DisplayName("Not find user by email")
	@Test
	void notFindUserByEmail() {
		UserEntity findedUser = userService.findByEmail("xxxx@hotmail.com");
		assertNull(findedUser);
	}	
	
	@DisplayName("Register a new user")
	@Test
	void registerNewUserAccount() {
		
		AuthSingUp accountDto = AuthSingUp.builder().firstName("Pascal")
										.lastName("Gozzi")
										.password("123456")
										.email("pascal@gmail.com").build();		
		
		UserEntity newUser = null;
		try {
			newUser = userService.registerNewUserAccount(accountDto);
		} catch (Exception e) {
			fail();
		}

		assertNotNull(newUser);
		assertNotNull(userService.findByEmail("pascal@gmail.com"));
	}
		
	@DisplayName("Not Register a user with exist email")
	@Test
	void notRegisterNewUserAccount() {
		
		AuthSingUp accountDto = AuthSingUp.builder().firstName("Paul")
				.lastName("Tre")
				.password("123456")
				.email("paul@hotmail.com").build();		

		UserEntity newUser = null;
		try {
			newUser = userService.registerNewUserAccount(accountDto);
			fail();
		} catch (EmailExistsException e) {
			assertEquals(ConstantsMessages.INVALID_USER_ALREADY_EXISTS, e.getMessage());			
			assertEquals("paul@hotmail.com", e.getEmail());
		}
		
		assertNull(newUser);
	}

	@DisplayName("Create token to user")
	@Test
	void createToken() {
		
		userService.createToken(user1, "token987", TypeEmailEnum.CONFIRMATION_USER);
		UserEntity u = userService.findUserByToken("token987", TypeEmailEnum.CONFIRMATION_USER);
		
		assertEquals(u.getId(), user1.getId());
	}

	@DisplayName("Register user and remove token")
	@Test
	void registerUserAndRemoveToken() {
		
		userService.createToken(user2, "token132654", TypeEmailEnum.CONFIRMATION_USER);		
		TokenEntity token = userService.findToken("token132654", TypeEmailEnum.CONFIRMATION_USER);
		
		assertFalse(user2.getEnabled());
		
		userService.registeredUserAndRemoveToken(token);
				
		UserEntity user = userService.findByEmail("paul@hotmail.com");		
		assertTrue(user.getEnabled());
		
		assertNull(userService.findToken("token987", TypeEmailEnum.CONFIRMATION_USER));		
	}
	
	@DisplayName("Find token")
	@Test
	void findToken() {
		
		userService.createToken(user2, "tokenabc", TypeEmailEnum.CONFIRMATION_USER);		
		assertNotNull(userService.findToken("tokenabc", TypeEmailEnum.CONFIRMATION_USER));
	}
	
	@DisplayName("Not Find token")
	@Test
	void notFindToken() {		
		assertNull(userService.findToken("token-dont-exists", TypeEmailEnum.CONFIRMATION_USER));
	}
	
	@DisplayName("Find token by user id and token")
	@Test
	void findTokenByUserIdAndToken() {
		
		userService.createToken(user2, "98765435211", TypeEmailEnum.CONFIRMATION_USER);		
		
		assertNotNull(userService.findByUserIdAndToken(user2.getId(), "98765435211"));
	}
	
	@DisplayName("Find token by User and type email")
	@Test
	void findTokenByUserAndTypeEmail() {
		
		userService.createToken(user2, "789654123", TypeEmailEnum.RESET_PASSWORD);		
		
		TokenEntity token = userService.findToken(user2, TypeEmailEnum.RESET_PASSWORD);
		
		assertNotNull(token);
		assertEquals("789654123", token.getToken());
	}
	
	@DisplayName("Save token")
	@Test
	void saveToken() {
		
		TokenEntity myToken = TokenEntity.builder().token("963258741").user(user2).typeEmail(TypeEmailEnum.RESET_PASSWORD).build();
		userService.saveToken(myToken);
				
		TokenEntity token = userService.findToken("963258741", TypeEmailEnum.RESET_PASSWORD);
		
		assertNotNull(token);
		assertEquals("963258741", token.getToken());
	}
	
	@DisplayName("Update password and remove token")
	@Test
	void updatePassword() {
		
		TokenEntity myToken = TokenEntity.builder().token("tokenresetpassword").user(user1).typeEmail(TypeEmailEnum.RESET_PASSWORD).build();
		userService.saveToken(myToken);
		
		UserEntity u1 = userService.findByEmail(user1.getEmail());	
		
		TokenEntity token = userService.findToken("tokenresetpassword", TypeEmailEnum.RESET_PASSWORD);	
		
		userService.updatePassword("123456789", token);		
	
		UserEntity u2 = userService.findByEmail(user1.getEmail());		
		assertNotEquals(u1.getPassword(), u2.getPassword());
		
		assertNull(userService.findToken("tokenresetpassword", TypeEmailEnum.RESET_PASSWORD));
	}
	
}
