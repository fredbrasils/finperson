package br.com.finperson.core.service;

import br.com.finperson.core.exception.EmailExistsException;
import br.com.finperson.domain.UserEntity;
import br.com.finperson.domain.enumm.TypeEmailEnum;
import br.com.finperson.security.domain.TokenEntity;
import br.com.finperson.security.domain.UserDTO;

public interface UserService extends BaseService<UserEntity, Long>{

	UserEntity findByEmail(String email);
	
	UserEntity registerNewUserAccount(UserDTO accountDto) throws EmailExistsException;
	
	UserEntity findUserByToken(String token,TypeEmailEnum typeEmail);
	 
    void saveRegisteredUser(UserEntity user);
 
    void createToken(UserEntity user, String token,TypeEmailEnum typeEmail);
 
    TokenEntity findToken(String token,TypeEmailEnum typeEmail);
    
    boolean emailExists(String email);

    TokenEntity findByUserIdAndToken(Long id, String token);

	void updatePassword(String password, UserEntity user);
}
