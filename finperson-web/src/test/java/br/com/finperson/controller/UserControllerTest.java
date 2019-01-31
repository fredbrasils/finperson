package br.com.finperson.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import br.com.finperson.core.exception.EmailExistsException;
import br.com.finperson.core.service.UserService;
import br.com.finperson.domain.UserEntity;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

	@Mock
    UserService userService;

    @InjectMocks
    UserController userController;

    MockMvc mockMvc;

    @Mock
    ApplicationEventPublisher eventPublisher;
    
    @BeforeEach
    void setUp() {
    	
        mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .build();
    }

    @DisplayName(value="Show the registration form's page")
    @Test
    void showRegistrationForm() throws Exception {
    	
        mockMvc.perform(get("/user/registration"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/registration"))
                .andExpect(model().attributeExists("user"));

    }

    @DisplayName(value="Register a new user")
    @Test
    void registerUserAccount() throws Exception {
    	
    	when(userService.registerNewUserAccount(ArgumentMatchers.any())).thenReturn(UserEntity.builder().id(1l).firstName("Fred").build());

        mockMvc.perform(post("/user/registration")
        		.param("firstName", "fred")
        		.param("lastName", "santos")
        		.param("email", "fredbrasils@hotmail.com")
        		.param("password", "123")
        		.param("matchingPassword", "123")
        		)
                .andExpect(status().isOk())
                .andExpect(view().name("user/successRegister"))
                .andExpect(model().attributeExists("user"))
                ;

        verify(userService).registerNewUserAccount(ArgumentMatchers.any());

    }
    
    @DisplayName(value="Throw exception when try send email to a new user")
    @Test
    void throwExcptionWhenRegisterUser() throws Exception {
    	
    	when(userService.registerNewUserAccount(ArgumentMatchers.any())).thenReturn(UserEntity.builder().id(1l).firstName("Fred").build());

    	Mockito.doCallRealMethod().when(eventPublisher).publishEvent(null);
    	    	
        mockMvc.perform(post("/user/registration")
        		.param("firstName", "fred")
        		.param("lastName", "santos")
        		.param("email", "fredbrasils@hotmail.com")
        		.param("password", "123")
        		.param("matchingPassword", "123")
        		)
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("user"))
                ;

        verify(userService).registerNewUserAccount(ArgumentMatchers.any());

    }
    
    @DisplayName(value="Not Register a user")
    @Test
    void notRegisterUserAccount() throws Exception {
    	
        mockMvc.perform(post("/user/registration")
        		.param("firstName", "fred")
        		.param("lastName", "santos")
        		.param("email", "fredbrasils@hotmail.com")
        		.param("password", "123")
        		.param("matchingPassword", "123")
        		)
                .andExpect(status().isOk())
                .andExpect(view().name("user/registration"))
                .andExpect(model().attributeExists("user"))
                ;

        verify(userService).registerNewUserAccount(ArgumentMatchers.any());

    }
    
    @DisplayName(value="Not Register a user because the password doesn't match.")
    @Test
    void notRegisterUserAccountBecauseNotMatchPassword() throws Exception {
    	
        mockMvc.perform(post("/user/registration")
        		.param("firstName", "fred")
        		.param("lastName", "santos")
        		.param("email", "fredbrasils@hotmail.com")
        		.param("password", "123")
        		.param("matchingPassword", "1234")
        		)
                .andExpect(status().isOk())
                .andExpect(view().name("user/registration"))
                .andExpect(model().attributeExists("user"))
                ;

    }
    
    @DisplayName(value="Not Register a user because the email exist.")
    @Test
    void notRegisterUserAccountBecauseExistEmail() throws Exception {
    	
    	when(userService.registerNewUserAccount(ArgumentMatchers.any())).thenThrow(EmailExistsException.class);

        mockMvc.perform(post("/user/registration")
        		.param("firstName", "fred")
        		.param("lastName", "santos")
        		.param("email", "fredbrasils@hotmail.com")
        		.param("password", "123")
        		.param("matchingPassword", "123")
        		)
                .andExpect(status().isOk())
                .andExpect(view().name("user/registration"))
                .andExpect(model().attributeExists("user"))
                ;

        verify(userService).registerNewUserAccount(ArgumentMatchers.any());

    }
    
}
