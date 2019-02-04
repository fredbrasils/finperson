package br.com.finperson.util.listener;

import java.util.Locale;

import org.springframework.context.ApplicationEvent;

import br.com.finperson.domain.UserEntity;
import br.com.finperson.domain.enumm.TypeEmailEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OnSendEmailEvent extends ApplicationEvent {
    
	private static final long serialVersionUID = 1L;
	
	private String appUrl;
    private Locale locale;
    private UserEntity user;
    private TypeEmailEnum typeEmail;
    private Object[] params;
 
    @Builder
    public OnSendEmailEvent(
    		UserEntity user, Locale locale, String appUrl, TypeEmailEnum typeEmail,Object[] params) {
        super(user);
         
        this.user = user;
        this.locale = locale;
        this.appUrl = appUrl;
        this.typeEmail = typeEmail;
        this.params = params;
    }
     
}
