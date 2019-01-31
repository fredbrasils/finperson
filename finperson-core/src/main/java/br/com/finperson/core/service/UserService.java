package br.com.finperson.core.service;

import br.com.finperson.core.exception.EmailExistsException;
import br.com.finperson.domain.UserEntity;
import br.com.finperson.security.domain.TokenEntity;
import br.com.finperson.security.domain.UserDTO;

public interface UserService extends BaseService<UserEntity, Long>{

	UserEntity findByEmail(String email);
	
	UserEntity registerNewUserAccount(UserDTO accountDto) throws EmailExistsException;
	
	UserEntity findUserByToken(String token);
	 
    void saveRegisteredUser(UserEntity user);
 
    void createToken(UserEntity user, String token);
 
    TokenEntity findToken(String token);
}
