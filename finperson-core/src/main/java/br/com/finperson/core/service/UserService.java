package br.com.finperson.core.service;

import br.com.finperson.core.exception.EmailExistsException;
import br.com.finperson.model.TokenEntity;
import br.com.finperson.model.UserEntity;
import br.com.finperson.model.enumm.TypeEmailEnum;
import br.com.finperson.model.payload.AuthSingUp;

public interface UserService extends BaseService<UserEntity, Long>{

	UserEntity findByEmail(String email);
	
	UserEntity registerNewUserAccount(AuthSingUp accountDto) throws EmailExistsException;
	
	UserEntity findUserByToken(String token,TypeEmailEnum typeEmail);
	 
    void saveRegisteredUser(UserEntity user);
 
    void createToken(UserEntity user, String token,TypeEmailEnum typeEmail);
 
    TokenEntity findToken(String token,TypeEmailEnum typeEmail);
    
    TokenEntity findToken(UserEntity user,TypeEmailEnum typeEmail);
    
    boolean emailExists(String email);

    TokenEntity findByUserIdAndToken(Long id, String token);

	void updatePassword(String password, UserEntity user);

	void saveToken(TokenEntity token);
}
