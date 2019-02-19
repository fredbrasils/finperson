package br.com.finperson.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import br.com.finperson.core.exception.EmailExistsException;
import br.com.finperson.core.service.UserService;
import br.com.finperson.model.TokenEntity;
import br.com.finperson.model.UserEntity;
import br.com.finperson.model.enumm.TypeEmailEnum;
import br.com.finperson.model.payload.AuthLogin;
import br.com.finperson.model.payload.AuthSingUp;
import br.com.finperson.model.payload.AuthUpdatePassword;
import br.com.finperson.model.payload.ResetPassword;
import br.com.finperson.security.JwtTokenProvider;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest extends AbstractRestControllerTest{

	@Mock
    UserService userService;

    @InjectMocks
    AuthController authController;

    MockMvc mockMvc;

    @Mock
    ApplicationEventPublisher eventPublisher;
    
    @Mock
    MessageSource messages;
    
    @Mock
    AuthenticationManager authenticationManager;
    
    @Mock
    JwtTokenProvider tokenProvider;
    
    @Mock
	Environment env;
    
    @BeforeEach
    void setUp() {
    	
        mockMvc = MockMvcBuilders
                .standaloneSetup(authController)
                .build();
    }

    @DisplayName(value="Autenticate user")
    @Test
    void autenticateUser() throws Exception {
    	
    	AuthLogin login = new AuthLogin();
    	login.setEmail("fred@hotmail.com");
    	login.setPassword("123");
        
    	when(authenticationManager.authenticate(ArgumentMatchers.any()))
    				.thenReturn(new UsernamePasswordAuthenticationToken("fred@email.com","123") );

    	when(tokenProvider.generateToken(ArgumentMatchers.any())).thenReturn("123");
    	
        mockMvc.perform(post("/api/auth/signin")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(asJsonString(login)))
               	.andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken", equalTo("123")))
                ;

    }

    @DisplayName(value="Not Autenticate user")
    @Test
    void notAutenticateUser() throws Exception {
    	
    	AuthLogin login = AuthLogin.builder().email("fred@hotmail.com").password("123").build();
        
    	when(authenticationManager.authenticate(null)).thenReturn(null);
    	
        mockMvc.perform(post("/api/auth/signin")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(asJsonString(login)))
               	.andExpect(status().isBadRequest())
                ;

    }
    
    @DisplayName(value="Register a new user")
    @Test
    void registerUserAccount() throws Exception {
    	
    	AuthSingUp singUp = AuthSingUp.builder().email("fred@hotmail.com").password("123")
    			.firstName("Fred").lastName("Santos").matchingPassword("123").build();
    	
    	when(userService.registerNewUserAccount(ArgumentMatchers.any())).thenReturn(UserEntity.builder().id(1l).firstName("Fred").build());

        mockMvc.perform(post("/api/auth/signup")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(asJsonString(singUp)))
                .andExpect(status().isCreated())
                ;

        verify(userService).registerNewUserAccount(ArgumentMatchers.any());

    }
    
    @DisplayName(value="Throw exception when try send email to a new user")
    @Test
    void throwExceptionWhenRegisterUser() throws Exception {
    	
    	AuthSingUp singUp = AuthSingUp.builder().email("fred@hotmail.com").password("123")
    			.firstName("Fred").lastName("Santos").matchingPassword("123").build();
    	
    	when(userService.registerNewUserAccount(ArgumentMatchers.any())).thenReturn(UserEntity.builder().id(1l).firstName("Fred").build());

    	Mockito.doCallRealMethod().when(eventPublisher).publishEvent(null);
    	    	
    	mockMvc.perform(post("/api/auth/signup")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(asJsonString(singUp)))
                .andExpect(status().isBadRequest())
                ;

        verify(userService).registerNewUserAccount(ArgumentMatchers.any());

    }
    
    @DisplayName(value="Not Register a user because first name is empty")
    @Test
    void notRegisterUserAccount() throws Exception {
    	
    	AuthSingUp singUp = AuthSingUp.builder().email("fred@hotmail.com").password("123")
    			.firstName("").lastName("Santos").matchingPassword("123").build();
    	
    	mockMvc.perform(post("/api/auth/signup")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(asJsonString(singUp)))
                .andExpect(status().isBadRequest())
                ;

        verify(userService, times(0)).registerNewUserAccount(ArgumentMatchers.any());

    }
    
    @DisplayName(value="Not Register a user because the password doesn't match.")
    @Test
    void notRegisterUserAccountBecauseNotMatchPassword() throws Exception {
    	
    	AuthSingUp singUp = AuthSingUp.builder().email("fred@hotmail.com").password("12345")
    			.firstName("Fred").lastName("Santos").matchingPassword("123").build();
    	
    	mockMvc.perform(post("/api/auth/signup")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(asJsonString(singUp)))
                .andExpect(status().isBadRequest())
                ;

        verify(userService, times(0)).registerNewUserAccount(ArgumentMatchers.any());

    }
    
    @DisplayName(value="Not Register a user because the email exist.")
    @Test
    void notRegisterUserAccountBecauseExistEmail() throws Exception {
    	
    	AuthSingUp singUp = AuthSingUp.builder().email("fred@hotmail.com").password("123")
    			.firstName("Fred").lastName("Santos").matchingPassword("123").build();
    	
    	when(userService.registerNewUserAccount(ArgumentMatchers.any())).thenThrow(EmailExistsException.class);

    	mockMvc.perform(post("/api/auth/signup")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(asJsonString(singUp)))
                .andExpect(status().isBadRequest())
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

        mockMvc.perform(get("/api/auth/registrationConfirmed")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)
        		.param("token", "123"))
                .andExpect(status().isOk())
                ;

        verify(userService).saveRegisteredUser(ArgumentMatchers.any());

    }
    
    @DisplayName(value="Confirmation register denied because the token is invalid.")
    @Test
    void invalidToken() throws Exception {
    	
    	when(userService.findToken(ArgumentMatchers.anyString(), ArgumentMatchers.eq(TypeEmailEnum.CONFIRMATION_USER))).thenReturn(null);
    	
    	when(messages.getMessage(anyString(),eq(null),ArgumentMatchers.any(Locale.class))).thenReturn("messages");

        mockMvc.perform(get("/api/auth/registrationConfirmed")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)
        		.param("token", "token123"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", equalTo(false)));

        verify(userService, times(0)).saveRegisteredUser(ArgumentMatchers.any());

    }
    
    @DisplayName(value="Confirmation register denied because the token is expired. A new token is sended.")
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
    	
    	mockMvc.perform(get("/api/auth/registrationConfirmed")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)
        		.param("token", "token123"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", equalTo(false)))
                ;

    	verify(userService, times(0)).saveRegisteredUser(ArgumentMatchers.any());

    }
    
    @DisplayName(value="Confirmation register denied because the token is expired and throws exception when tried send new token")
    @Test
    void throwExceptionWhenExpiredToken() throws Exception {
    	
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
    	Mockito.doCallRealMethod().when(eventPublisher).publishEvent(null);
    	
    	mockMvc.perform(get("/api/auth/registrationConfirmed")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)
        		.param("token", "token123"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", equalTo(false)));

    	verify(userService, times(0)).saveRegisteredUser(ArgumentMatchers.any());

    }
    
    @DisplayName(value="Send email to confirme reset of the password")
    @Test
    void sendEmailToConfirmResetPassword() throws Exception {
    	
    	UserEntity user = UserEntity.builder().id(1l)
				.firstName("Fred")
				.lastName("Brasil")
				.email("fredbrasils@hotmail.com")
				.build();
		
    	ResetPassword resetPass = ResetPassword.builder()
    			.email("fredbrasils@hotmail.com")
    			.build();
    	
    	when(userService.findByEmail(anyString())).thenReturn(user);
    	
    	mockMvc.perform(post("/api/auth/resetPassword")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(asJsonString(resetPass)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", equalTo(true)));

    }
    
    @DisplayName(value="Not Send email to a user that doesn't exists")
    @Test
    void notSendEmailUserDoesntExists() throws Exception {
    	
    	ResetPassword resetPass = ResetPassword.builder()
    			.email("fredbrasils@hotmail.com")
    			.build();
    	
    	when(userService.findByEmail(anyString())).thenReturn(null);
    	
    	mockMvc.perform(post("/api/auth/resetPassword")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(asJsonString(resetPass)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", equalTo(false)));

    	verify(eventPublisher, times(0)).publishEvent(ArgumentMatchers.any());
    }
    
    @DisplayName(value="Not Send email to a invalid email")
    @Test
    void notSendEmailToInvalidEmail() throws Exception {
    	
    	ResetPassword resetPass = ResetPassword.builder()
    			.email("email.example.com")
    			.build();
    	
    	mockMvc.perform(post("/api/auth/resetPassword")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(asJsonString(resetPass)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", equalTo(false)));

    	verify(userService, times(0)).findByEmail(anyString());
    	verify(eventPublisher, times(0)).publishEvent(ArgumentMatchers.any());
    	
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
    	
    	ResetPassword resetPass = ResetPassword.builder()
    			.email("fredbrasils@hotmail.com")
    			.build();
    	
    	mockMvc.perform(post("/api/auth/resetPassword")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(asJsonString(resetPass)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", equalTo(false)));
    	
    	verify(userService, times(1)).findByEmail(anyString());
    	verify(eventPublisher, times(1)).publishEvent(ArgumentMatchers.any());
    }
    
    @DisplayName(value="Update password")
    @Test
    void updatePassword() throws Exception {
    	
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
    	
    	when(userService.findByUserIdAndToken(ArgumentMatchers.anyLong(), anyString())).thenReturn(token);
    	
    	AuthUpdatePassword authUpdate = AuthUpdatePassword.builder()
    			.email("fredbrasils@hotmail.com")
    			.firstName("Fred")
				.lastName("Brasil")
				.matchingPassword("123")
				.password("123")
				.id(1l)
				.token("token1234")
    			.build();
    	
    	mockMvc.perform(post("/api/auth/updatePassword")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(asJsonString(authUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", equalTo(true)));
    	
        verify(userService).findByUserIdAndToken(ArgumentMatchers.anyLong(), anyString());
        verify(userService).updatePassword(anyString(), any(UserEntity.class));
    }
    
    @DisplayName(value="Don't update password's because token is invalid")
    @Test
    void notUpdatePasswordBecauseTokenInvalid() throws Exception {
    	
    	when(userService.findByUserIdAndToken(ArgumentMatchers.anyLong(), anyString())).thenReturn(null);

    	AuthUpdatePassword authUpdate = AuthUpdatePassword.builder()
    			.email("fredbrasils@hotmail.com")
    			.firstName("Fred")
				.lastName("Brasil")
				.matchingPassword("123")
				.password("123")
				.id(1l)
				.token("token1234")
    			.build();
    	
    	mockMvc.perform(post("/api/auth/updatePassword")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(asJsonString(authUpdate)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", equalTo(false)));
    	
        verify(userService).findByUserIdAndToken(ArgumentMatchers.anyLong(), anyString());
        verify(userService, times(0)).updatePassword(anyString(), any(UserEntity.class));

    }
    
    @DisplayName(value="Not Update password's user because password doesn't match.")
    @Test
    void notUpdatePasswordNotMatch() throws Exception {
    	
    	AuthUpdatePassword authUpdate = AuthUpdatePassword.builder()
    			.email("fredbrasils@hotmail.com")
    			.firstName("Fred")
				.lastName("Brasil")
				.matchingPassword("1231434123")
				.password("123")
				.id(1l)
				.token("token1234")
    			.build();
    	
    	mockMvc.perform(post("/api/auth/updatePassword")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(asJsonString(authUpdate)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", equalTo(false)));
    	
        verify(userService, times(0)).findByUserIdAndToken(ArgumentMatchers.anyLong(), anyString());
        verify(userService, times(0)).updatePassword(anyString(), any(UserEntity.class));

    }
    
}
