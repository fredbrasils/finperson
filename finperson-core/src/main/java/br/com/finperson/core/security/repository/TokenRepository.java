package br.com.finperson.core.security.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import br.com.finperson.domain.UserEntity;
import br.com.finperson.domain.enumm.TypeEmailEnum;
import br.com.finperson.security.domain.TokenEntity;

public interface TokenRepository extends CrudRepository<TokenEntity, Long>{

	TokenEntity findByUserAndTypeEmail(UserEntity user, TypeEmailEnum typeEmail);

	TokenEntity findByTokenAndTypeEmail(String token, TypeEmailEnum typeEmail);

	@Query("SELECT t FROM TokenEntity t inner join fetch t.user u WHERE u.id = :id and t.token = :token")
	TokenEntity findByUserIdAndToken(Long id, String token);
}
