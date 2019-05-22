package br.com.finperson.core.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.finperson.model.ReminderTimeEntity;

public interface ReminderTimeRepository extends CrudRepository<ReminderTimeEntity, Long>{
	
}
