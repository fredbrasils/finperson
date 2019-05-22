package br.com.finperson.core.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.finperson.model.ReminderEntity;

public interface ReminderRepository extends CrudRepository<ReminderEntity, Long>{
	
}
