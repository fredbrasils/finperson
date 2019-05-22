package br.com.finperson.core.service.impl;

import java.util.HashSet;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.finperson.core.exception.EmailExistsException;
import br.com.finperson.core.repository.RoleRepository;
import br.com.finperson.core.repository.TokenRepository;
import br.com.finperson.core.repository.UserRepository;
import br.com.finperson.core.service.UserService;
import br.com.finperson.model.RoleEntity;
import br.com.finperson.model.TokenEntity;
import br.com.finperson.model.UserEntity;
import br.com.finperson.model.enumm.RoleEnum;
import br.com.finperson.model.enumm.TypeEmailEnum;
import br.com.finperson.model.payload.AuthSingUp;
import br.com.finperson.util.ConstantsMessages;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl extends BaseServiceImpl<UserEntity, Long> implements UserService {

	private PasswordEncoder passwordEncoder;
	private UserRepository userRepository;
	private RoleRepository roleRepository;
	private TokenRepository tokenRepository;
	
	public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, 
			TokenRepository tokenRepository, PasswordEncoder passwordEncoder) {
		
		super(userRepository);
		log.debug("Create UserServiceImpl");
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.tokenRepository = tokenRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public UserEntity findByEmail(String email) {
		log.debug("Recover user by email");
		return userRepository.findByEmail(email);
	}

	@Transactional
    @Override
    public UserEntity registerNewUserAccount(AuthSingUp accountDto) 
      throws EmailExistsException {
         
		log.debug("Register user");
		
        if (emailExists(accountDto.getEmail())) {   
            throw new EmailExistsException(ConstantsMessages.INVALID_USER_ALREADY_EXISTS, accountDto.getEmail());
        }
        
        UserEntity user = new UserEntity();    
        user.setFirstName(accountDto.getFirstName());
        user.setLastName(accountDto.getLastName());
        user.setPassword(passwordEncoder.encode(accountDto.getPassword()));
        user.setEmail(accountDto.getEmail());
        user.setEnabled(false);
        user.setNonLocked(true);
        
        Set<RoleEntity> roles = new HashSet<>(); 
        roles.add(roleRepository.findByRole(RoleEnum.ROLE_USER));
    		
        user.setRoles(roles);
        return userRepository.save(user);       
    }

	private Boolean emailExists(String email) {
		log.debug("Verify if email exists");
		return (userRepository.findByEmail(email) != null);
	}

	@Override
	public UserEntity findUserByToken(String token,TypeEmailEnum typeEmail) {
		return tokenRepository.findByTokenAndTypeEmail(token, typeEmail).getUser();
	}

	@Override
	@Transactional
	public void registeredUserAndRemoveToken(TokenEntity token) {
		UserEntity user = token.getUser();
		user.setEnabled(true);		
		userRepository.save(user);
		tokenRepository.delete(token);
	}

	@Override
	public void createToken(UserEntity user, String token,TypeEmailEnum typeEmail) {
		TokenEntity myToken = TokenEntity.builder().token(token).user(user).typeEmail(typeEmail).build();
        tokenRepository.save(myToken);
	}

	@Override
	public TokenEntity findToken(String token,TypeEmailEnum typeEmail) {
		return tokenRepository.findByTokenAndTypeEmail(token,typeEmail);
	}

	@Override
	public TokenEntity findByUserIdAndToken(Long id, String token) {
		return tokenRepository.findByUserIdAndToken(id,token);
	}

	@Transactional
    @Override
	public void updatePassword(String password, TokenEntity token) {
		
		log.debug("Update password");
		
        UserEntity userRegistered = findById(token.getUser().getId());
        
        userRegistered.setPassword(passwordEncoder.encode(password));
        	
        userRepository.save(userRegistered);
        
        tokenRepository.delete(token);
	}

	@Override
	public TokenEntity findToken(UserEntity user, TypeEmailEnum typeEmail) {
		return tokenRepository.findByUserAndTypeEmail(user,typeEmail);
	}
	
	@Override
	public void saveToken(TokenEntity token) {
		tokenRepository.save(token);
	}

}
