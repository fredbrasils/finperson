package br.com.finperson.core.service;

import br.com.finperson.domain.RoleEntity;
import br.com.finperson.domain.enumm.RoleEnum;

public interface RoleService extends BaseService<RoleEntity, Long>{

	RoleEntity findByRole(RoleEnum role);
}
