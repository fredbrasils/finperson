package br.com.finperson.util.listener;

import java.util.Locale;

import org.springframework.context.ApplicationEvent;

import br.com.finperson.domain.UserEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OnRegistrationCompleteEvent extends ApplicationEvent {
    
	private static final long serialVersionUID = 1L;
	
	private String appUrl;
    private Locale locale;
    private UserEntity user;
 
    @Builder
    public OnRegistrationCompleteEvent(
    		UserEntity user, Locale locale, String appUrl) {
        super(user);
         
        this.user = user;
        this.locale = locale;
        this.appUrl = appUrl;
    }
     
}
