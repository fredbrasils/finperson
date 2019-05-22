package br.com.finperson.core.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.finperson.model.CurrencyEntity;

public interface CurrencyRepository extends CrudRepository<CurrencyEntity, Long>{
	
}
