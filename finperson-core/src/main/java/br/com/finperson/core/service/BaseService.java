package br.com.finperson.core.service;

import java.util.Set;

public interface BaseService <T, ID> {

	T findById(ID id);
	
	T save(T entity);
	
	Set<T> findAll();
	
	void delete(T entity);
	
	void deleteById(ID id);
}
