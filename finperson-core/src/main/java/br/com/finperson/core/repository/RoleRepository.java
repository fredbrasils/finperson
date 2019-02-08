package br.com.finperson.core.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.finperson.model.RoleEntity;
import br.com.finperson.model.enumm.RoleEnum;

public interface RoleRepository extends CrudRepository<RoleEntity, Long>{

	RoleEntity findByRole(RoleEnum role);
}
