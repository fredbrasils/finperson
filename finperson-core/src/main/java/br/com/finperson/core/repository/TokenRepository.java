package br.com.finperson.core.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import br.com.finperson.model.TokenEntity;
import br.com.finperson.model.UserEntity;
import br.com.finperson.model.enumm.TypeEmailEnum;

public interface TokenRepository extends CrudRepository<TokenEntity, Long>{

	TokenEntity findByUserAndTypeEmail(UserEntity user, TypeEmailEnum typeEmail);

	TokenEntity findByTokenAndTypeEmail(String token, TypeEmailEnum typeEmail);

	@Query("SELECT t FROM TokenEntity t inner join fetch t.user u WHERE u.id = :id and t.token = :token")
	TokenEntity findByUserIdAndToken(Long id, String token);

}
