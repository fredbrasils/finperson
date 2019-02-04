package br.com.finperson.core.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.finperson.domain.UserEntity;

public interface UserRepository extends CrudRepository<UserEntity, Long>{

	UserEntity findByEmail(String email);

}
