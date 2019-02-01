package br.com.finperson.util.listener;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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

@ExtendWith(MockitoExtension.class)
class RegistrationListenerTest {

	@Mock
	UserService userService;

	@Mock
	MessageSource messages;

	@Mock
	JavaMailSender mailSender;

	@Mock
    Environment env;
	
	@InjectMocks
	RegistrationListener registrationListener;

	OnRegistrationCompleteEvent event;

	@BeforeEach
    void setUp() {
    	
		event = OnRegistrationCompleteEvent.builder()
				.appUrl("http://localhost")
				.user(UserEntity.builder().build())
				.build();
    }
	
	@DisplayName(value="Test registration listener to send email to user.")
    @Test
    void registrationProcess() throws Exception {

		doReturn("messages").when(messages).getMessage(anyString(),eq(null),any());
		
		registrationListener.onApplicationEvent(event);
		
    	verify(userService, times(1)).createToken(ArgumentMatchers.any(UserEntity.class),ArgumentMatchers.anyString());
    	
    	verify(mailSender, times(1)).send(any(SimpleMailMessage.class));

    }
}
