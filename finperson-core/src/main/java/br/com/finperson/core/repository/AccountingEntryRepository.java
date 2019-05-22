package br.com.finperson.core.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.finperson.model.AccountingEntryEntity;

public interface AccountingEntryRepository extends CrudRepository<AccountingEntryEntity, Long>{
	
}
