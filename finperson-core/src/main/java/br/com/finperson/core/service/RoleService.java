package br.com.finperson.core.service;

import br.com.finperson.model.RoleEntity;
import br.com.finperson.model.enumm.RoleEnum;

public interface RoleService extends BaseService<RoleEntity, Long>{

	RoleEntity findByRole(RoleEnum role);
}
