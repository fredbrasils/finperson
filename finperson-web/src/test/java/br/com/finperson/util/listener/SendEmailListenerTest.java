package br.com.finperson.util.listener;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import br.com.finperson.core.service.UserService;
import br.com.finperson.domain.UserEntity;
import br.com.finperson.domain.enumm.TypeEmailEnum;
import br.com.finperson.security.domain.TokenEntity;

@ExtendWith(MockitoExtension.class)
class SendEmailListenerTest {

	@Mock
	UserService userService;

	@Mock
	MessageSource messages;

	@Mock
	JavaMailSender mailSender;

	@Mock
    Environment env;
	
	@InjectMocks
	SendEmailListener registrationListener;

	OnSendEmailEvent event;

	UserEntity returnUser;
	
	TokenEntity returnToken;
	
	@BeforeEach
    void setUp() {
    	
		returnUser = UserEntity.builder().id(1l)
				.firstName("Fred")
				.lastName("Brasil")
				.email("fredbrasils@hotmail.com")
				.build();
		
		returnToken = TokenEntity.builder()
				.id(1l)
				.user(returnUser)
				.token("token1234")
				.build();
		
		event = OnSendEmailEvent.builder()
				.appUrl("http://localhost")
				.user(returnUser)
				.typeEmail(TypeEmailEnum.CONFIRMATION_USER)
				.params(new Object[] {"1"})
				.build();
    }
	
	@DisplayName(value="Test email listener to send email to user.")
    @Test
    void registrationProcess() throws Exception {

		doReturn("messages").when(messages).getMessage(anyString(),eq(null),any());
		
		when(userService.findToken(any(UserEntity.class), any(TypeEmailEnum.class))).thenReturn(null);
		
		registrationListener.onApplicationEvent(event);
		
    	verify(userService, times(1)).createToken(ArgumentMatchers.any(UserEntity.class),ArgumentMatchers.anyString(),ArgumentMatchers.any(TypeEmailEnum.class));
    	
    	verify(mailSender, times(1)).send(any(SimpleMailMessage.class));

    }
	
	@DisplayName(value="Test email listener to send email to update token.")
    @Test
    void updateProcess() throws Exception {

		doReturn("messages").when(messages).getMessage(anyString(),eq(null),any());
		
		when(userService.findToken(any(UserEntity.class), any(TypeEmailEnum.class))).thenReturn(returnToken);
		
		registrationListener.onApplicationEvent(event);
		
    	verify(userService, times(1)).saveToken(ArgumentMatchers.any(TokenEntity.class));
    	
    	verify(mailSender, times(1)).send(any(SimpleMailMessage.class));

    }
}
