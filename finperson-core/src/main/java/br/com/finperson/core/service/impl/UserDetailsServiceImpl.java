package br.com.finperson.core.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.finperson.core.repository.UserRepository;
import br.com.finperson.model.RoleEntity;
import br.com.finperson.model.UserEntity;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepository userRepository;
	
	public UserDetailsServiceImpl(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}
	
	@Override
	public UserDetails loadUserByUsername(String email) {
		UserEntity user = userRepository.findByEmail(email);
		if (user == null) {
			throw new UsernameNotFoundException("No user found with username: " + email);
		}
		boolean accountNonExpired = true;
		boolean credentialsNonExpired = true;

		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
				user.getEnabled(), accountNonExpired, credentialsNonExpired, user.getNonLocked(), getAuthorities(user.getRoles()));
	}

	public static List<GrantedAuthority> getAuthorities (Set<RoleEntity> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (RoleEntity role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getRole().name()));
        }
        return authorities;
    }
}
