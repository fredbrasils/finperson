package br.com.finperson.core.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.finperson.model.ConfigEntity;

public interface ConfigRepository extends CrudRepository<ConfigEntity, Long>{
	
}
