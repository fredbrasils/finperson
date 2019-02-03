package br.com.finperson.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Calendar;
import java.util.Locale;

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
import org.springframework.context.MessageSource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import br.com.finperson.core.exception.EmailExistsException;
import br.com.finperson.core.service.UserService;
import br.com.finperson.domain.UserEntity;
import br.com.finperson.domain.enumm.TypeEmailEnum;
import br.com.finperson.security.domain.TokenEntity;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

	@Mock
    UserService userService;

    @InjectMocks
    UserController userController;

    MockMvc mockMvc;

    @Mock
    ApplicationEventPublisher eventPublisher;
    
    @Mock
    MessageSource messages;
    
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
    
    @DisplayName(value="Confirmation register of a new user.")
    @Test
    void confirmRegisterUser() throws Exception {
    	
    	UserEntity user = UserEntity.builder().id(1l)
				.firstName("Fred")
				.lastName("Brasil")
				.email("fredbrasils@hotmail.com")
				.build();
		
    	TokenEntity token = TokenEntity.builder()
				.id(1l)
				.user(user)
				.token("token1234")
				.build();
		
    	when(userService.findToken(ArgumentMatchers.anyString(), ArgumentMatchers.eq(TypeEmailEnum.CONFIRMATION_USER))).thenReturn(token);

        mockMvc.perform(get("/user/registrationConfirm")
        		.param("token", "token123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/login"));

        verify(userService).saveRegisteredUser(ArgumentMatchers.any());

    }
    
    @DisplayName(value="Confirmation register denied because the token is invalid.")
    @Test
    void invalidToken() throws Exception {
    	
    	when(userService.findToken(ArgumentMatchers.anyString(), ArgumentMatchers.eq(TypeEmailEnum.CONFIRMATION_USER))).thenReturn(null);
    	
    	when(messages.getMessage(anyString(),eq(null),ArgumentMatchers.any(Locale.class))).thenReturn("messages");

        mockMvc.perform(get("/user/registrationConfirm")
        		.param("token", "token123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().attributeExists("message"))
                .andExpect(view().name("redirect:/badUser"));

        verify(userService, times(0)).saveRegisteredUser(ArgumentMatchers.any());

    }
    
    @DisplayName(value="Confirmation register denied because the token is expired.")
    @Test
    void expiredToken() throws Exception {
    	
    	UserEntity user = UserEntity.builder().id(1l)
				.firstName("Fred")
				.lastName("Brasil")
				.email("fredbrasils@hotmail.com")
				.build();
		
    	TokenEntity token = TokenEntity.builder()
				.id(1l)
				.user(user)
				.token("token1234")
				.build();
		
    	Calendar calendar = Calendar.getInstance();
    	calendar.set(Calendar.YEAR, 2018);
    	token.setExpiryDate(calendar.getTime());
    	
    	when(userService.findToken(ArgumentMatchers.anyString(), ArgumentMatchers.eq(TypeEmailEnum.CONFIRMATION_USER))).thenReturn(token);

    	when(messages.getMessage(anyString(),eq(null),ArgumentMatchers.any(Locale.class))).thenReturn("messages");
    	
    	mockMvc.perform(get("/user/registrationConfirm")
        		.param("token", "token123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().attributeExists("message"))
                .andExpect(view().name("redirect:/badUser"));

    	verify(userService, times(0)).saveRegisteredUser(ArgumentMatchers.any());

    }
    
    @DisplayName(value="Show 'I forgot password' page")
    @Test
    void showIForgotPasswordForm() throws Exception {
    	
        mockMvc.perform(get("/user/forgotPassword"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/forgotPassword"))
                .andExpect(model().attributeExists("resetPass"));

    }
    
    @DisplayName(value="Send email to confirme reset of the password")
    @Test
    void sendEmailToConfirmResetPassword() throws Exception {
    	
    	UserEntity user = UserEntity.builder().id(1l)
				.firstName("Fred")
				.lastName("Brasil")
				.email("fredbrasils@hotmail.com")
				.build();
		
    	when(userService.findByEmail(anyString())).thenReturn(user);
    	
        mockMvc.perform(post("/user/messageResetPassword")
        		.param("email", "fredbrasils@hotmail.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/messageResetPassword"));

    }
    
    @DisplayName(value="Not Send email to a user that doesn't exists")
    @Test
    void notSendEmailUserDoesntExists() throws Exception {
    	
    	when(userService.findByEmail(anyString())).thenReturn(null);
    	
        mockMvc.perform(post("/user/messageResetPassword")
        		.param("email", "xxxx@hotmail.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/forgotPassword"))
                .andExpect(model().attributeExists("resetPass"));

    }
    
    @DisplayName(value="Not Send email to a invalid email")
    @Test
    void notSendEmailToInvalidEmail() throws Exception {
    	
        mockMvc.perform(post("/user/messageResetPassword")
        		.param("email", "email.example.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/forgotPassword"))
                .andExpect(model().attributeExists("resetPass"));

        verify(userService, times(0)).emailExists(ArgumentMatchers.anyString());
    }
    
    @DisplayName(value="Throw exception when try send email to reset password")
    @Test
    void throwExceptionWhenTrySendEmailToResetPassword() throws Exception {
    	
    	UserEntity user = UserEntity.builder().id(1l)
				.firstName("Fred")
				.lastName("Brasil")
				.email("fredbrasils@hotmail.com")
				.build();
		
    	when(userService.findByEmail(anyString())).thenReturn(user);
    	
    	Mockito.doCallRealMethod().when(eventPublisher).publishEvent(null);
    	
        mockMvc.perform(post("/user/messageResetPassword")
        		.param("email", "fredbrasils@hotmail.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/forgotPassword"))
                .andExpect(model().attributeExists("resetPass"));

    }
}
