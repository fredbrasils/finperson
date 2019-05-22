package br.com.finperson.core.service.impl;

import org.springframework.stereotype.Service;

import br.com.finperson.core.repository.ConfigRepository;
import br.com.finperson.core.service.ConfigService;
import br.com.finperson.model.ConfigEntity;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ConfigServiceImpl extends BaseServiceImpl<ConfigEntity,Long> implements ConfigService{
	
	private ConfigRepository configRepository;	
	
	public ConfigServiceImpl(ConfigRepository configRepository) {
		super(configRepository);
		this.configRepository = configRepository;
	}	
	

}
