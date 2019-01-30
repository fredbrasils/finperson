package br.com.finperson.core.service.impl;

import java.util.HashSet;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.finperson.core.repository.EmailExistsException;
import br.com.finperson.core.repository.RoleRepository;
import br.com.finperson.core.repository.UserRepository;
import br.com.finperson.core.service.UserService;
import br.com.finperson.domain.RoleEntity;
import br.com.finperson.domain.UserEntity;
import br.com.finperson.domain.enumm.RoleEnum;
import br.com.finperson.security.domain.UserDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl extends BaseServiceImpl<UserEntity, Long> implements UserService {

	private PasswordEncoder passwordEncoder;
	private UserRepository userRepository;
	private RoleRepository roleRepository;

	public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
		super(userRepository);
		log.debug("Create UserServiceImpl");
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public UserEntity findByEmail(String email) {
		log.debug("Recover user by email");
		return userRepository.findByEmail(email);
	}

	@Transactional
    @Override
    public UserEntity registerNewUserAccount(UserDTO accountDto) 
      throws EmailExistsException {
         
		log.debug("Register user");
		
        if (emailExists(accountDto.getEmail())) {   
            throw new EmailExistsException(
              "There is an account with that email address:"  + accountDto.getEmail());
        }
        UserEntity user = new UserEntity();    
        user.setFirstName(accountDto.getFirstName());
        user.setLastName(accountDto.getLastName());
        user.setPassword(passwordEncoder.encode(accountDto.getPassword()));
        user.setEmail(accountDto.getEmail());
        user.setEnabled(true);
        user.setNonLocked(true);
        
        Set<RoleEntity> roles = new HashSet<RoleEntity>(); 
        roles.add(roleRepository.findByRole(RoleEnum.ROLE_ADIM));
    		
        user.setRoles(roles);
        return userRepository.save(user);       
    }

	private boolean emailExists(String email) {
		
		log.debug("Verify if email exists");
		
		UserEntity user = userRepository.findByEmail(email);
		if (user != null) {
			return true;
		}
		return false;
	}

}
