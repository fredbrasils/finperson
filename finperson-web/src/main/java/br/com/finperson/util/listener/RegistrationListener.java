package br.com.finperson.util.listener;

import java.util.UUID;

import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import br.com.finperson.core.service.UserService;
import br.com.finperson.domain.UserEntity;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

	private UserService userService;

	private MessageSource messages;

	private JavaMailSender mailSender;

    private Environment env;
	
	public RegistrationListener(UserService userService, MessageSource messages, JavaMailSender mailSender,
			Environment env) {
		super();
		this.userService = userService;
		this.messages = messages;
		this.mailSender = mailSender;
		this.env = env;
	}

	@Override
	public void onApplicationEvent(OnRegistrationCompleteEvent event) {
		this.confirmRegistration(event);
	}

	private void confirmRegistration(OnRegistrationCompleteEvent event) {
		UserEntity user = event.getUser();
		String token = UUID.randomUUID().toString();
		userService.createToken(user, token);

		SimpleMailMessage email = constructEmailMessage(event, user, token);
		mailSender.send(email);
	}
	
	private final SimpleMailMessage constructEmailMessage(final OnRegistrationCompleteEvent event, final UserEntity user, final String token) {
        final String recipientAddress = user.getEmail();
        final String subject = "Registration Confirmation";
        final String confirmationUrl = event.getAppUrl() + "/user/registrationConfirm?token=" + token;
        final String message = messages.getMessage("message.regSucc", null, event.getLocale());
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + " \r\n" + confirmationUrl);
        email.setFrom(env.getProperty("support.email"));
        return email;
    }
}
