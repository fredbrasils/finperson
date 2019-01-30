package br.com.finperson.core.service;

import br.com.finperson.core.repository.EmailExistsException;
import br.com.finperson.domain.UserEntity;
import br.com.finperson.security.domain.UserDTO;

public interface UserService extends BaseService<UserEntity, Long>{

	UserEntity findByEmail(String email);
	
	UserEntity registerNewUserAccount(UserDTO accountDto) throws EmailExistsException;
}
