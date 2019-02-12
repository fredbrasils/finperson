package br.com.finperson.util.listener;

import java.util.UUID;

import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import br.com.finperson.core.service.UserService;
import br.com.finperson.model.TokenEntity;
import br.com.finperson.model.UserEntity;

@Component
public class SendEmailListener implements ApplicationListener<OnSendEmailEvent> {

	private static final String SUPPORT_EMAIL = "support.email";
	
	private UserService userService;

	private MessageSource messages;

	private JavaMailSender mailSender;

    private Environment env;
	
	public SendEmailListener(UserService userService, MessageSource messages, JavaMailSender mailSender,
			Environment env) {
		super();
		this.userService = userService;
		this.messages = messages;
		this.mailSender = mailSender;
		this.env = env;
	}

	@Override
	public void onApplicationEvent(OnSendEmailEvent event) {
		this.createEmail(event);
	}

	private void createEmail(OnSendEmailEvent event) {
		UserEntity user = event.getUser();
		String token = UUID.randomUUID().toString();
		
		TokenEntity tk = userService.findToken(user, event.getTypeEmail());
		if(tk == null) {
			userService.createToken(user, token, event.getTypeEmail());
		}else {
			TokenEntity myToken = TokenEntity.builder().id(tk.getId()).token(token).user(user).typeEmail(event.getTypeEmail()).build();
			userService.saveToken(myToken);
		}

		SimpleMailMessage email = constructEmailMessage(event, user, token);
		mailSender.send(email);
	}
	
	private final SimpleMailMessage constructEmailMessage(final OnSendEmailEvent event, final UserEntity user, final String token) {
        final String recipientAddress = user.getEmail();
        final String subject = messages.getMessage(event.getTypeEmail().getSubject(), null, event.getLocale());
        final String confirmationUrl = event.getAppUrl() + addParams(event.getTypeEmail().getUrl(), token, event.getParams());
        final String message = messages.getMessage(event.getTypeEmail().getMessage(), new String[] {confirmationUrl}, event.getLocale());
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message);
        email.setFrom(env.getProperty(SUPPORT_EMAIL));
        return email;
    }

	private String addParams(String url, String token, Object[] params) {
		
		int count = 0;
		url = url.replace("{"+count+"}", token);
		
		if(params != null) {
			for(Object param : params) {
				count++;
				url = url.replace("{"+count+"}", param.toString());
			}
		}		
		
		return url;
	}
	
	
}
