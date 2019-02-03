package br.com.finperson.config;

import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import br.com.finperson.core.service.security.impl.UserDetailsServiceImpl;
import br.com.finperson.util.ConstantsURL;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer{

	private UserDetailsServiceImpl userDetailsService;
	
	public SecurityConfig(UserDetailsServiceImpl userDetailsService) {
		super();
		this.userDetailsService = userDetailsService;
	}

	@Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers(ConstantsURL.SLASH, ConstantsURL.SLASH + ConstantsURL.INDEX
                		,"/user/registration"
                		,"/user/forgotPassword"
                		,"/user/messageResetPassword"
                		,"/user/registrationConfirm").permitAll()
                .anyRequest().authenticated()
                .and()
            .formLogin()
                .loginPage(ConstantsURL.SLASH + ConstantsURL.LOGIN)
                .usernameParameter("username")
                .passwordParameter("password")
                .permitAll()
                .and()
            .logout()
            .logoutSuccessUrl(ConstantsURL.SLASH + ConstantsURL.INDEX)
            .deleteCookies("JSESSIONID")
            .permitAll();
    }
	 
	@Override
	protected void configure(AuthenticationManagerBuilder auth) 
	  throws Exception {
		
		auth.authenticationProvider(authProvider());
	}
	
	@Bean
	public DaoAuthenticationProvider authProvider() {
	    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
	    authProvider.setUserDetailsService(userDetailsService);
	    authProvider.setPasswordEncoder(passwordEncoder());
	    return authProvider;
	}
	
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController(ConstantsURL.SLASH + ConstantsURL.INDEX).setViewName(ConstantsURL.INDEX);
        registry.addViewController(ConstantsURL.SLASH).setViewName(ConstantsURL.INDEX);
        registry.addViewController(ConstantsURL.SLASH + ConstantsURL.LOGIN).setViewName(ConstantsURL.LOGIN);
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.US);
        return slr;
    }
    
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }
}
