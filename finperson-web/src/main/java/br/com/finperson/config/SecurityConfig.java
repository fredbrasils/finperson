package br.com.finperson.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import br.com.finperson.core.service.security.impl.UserDetailsServiceImpl;
import br.com.finperson.util.ConstantsURL;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer{

	@Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers(ConstantsURL.SLASH, ConstantsURL.SLASH + ConstantsURL.INDEX,"/user/**").permitAll()
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

	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	 
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
	
	/*
    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        UserDetails user =
             User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(user);
    }
    */
    
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
}
