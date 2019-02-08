package br.com.finperson.core.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.finperson.model.UserEntity;

public interface UserRepository extends CrudRepository<UserEntity, Long>{

	UserEntity findByEmail(String email);

	/*
	 * Optional<User> findByUsernameOrEmail(String username, String email);

    List<User> findByIdIn(List<Long> userIds);

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
	 */
}
