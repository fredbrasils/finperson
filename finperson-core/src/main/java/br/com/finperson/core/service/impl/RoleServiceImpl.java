package br.com.finperson.core.service.impl;

import org.springframework.stereotype.Service;

import br.com.finperson.core.repository.RoleRepository;
import br.com.finperson.core.service.RoleService;
import br.com.finperson.domain.RoleEntity;
import br.com.finperson.domain.enumm.RoleEnum;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RoleServiceImpl extends BaseServiceImpl<RoleEntity,Long> implements RoleService{
	
	private RoleRepository roleRepository;	
	
	public RoleServiceImpl(RoleRepository roleRepository) {
		super(roleRepository);
		log.debug("Create RoleServiceImpl");
		this.roleRepository = roleRepository;
	}

	@Override
	public RoleEntity findByRole(RoleEnum role) {
		return roleRepository.findByRole(role);
	}	
	

}
