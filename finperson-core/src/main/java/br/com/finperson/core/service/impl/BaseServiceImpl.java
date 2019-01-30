package br.com.finperson.core.service.impl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import br.com.finperson.core.service.BaseService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public abstract class BaseServiceImpl<T, ID> implements BaseService<T, ID>{
	
	private final CrudRepository<T, ID> crudRepository;
	
	public BaseServiceImpl(CrudRepository<T, ID> crudRepository) {
		log.debug("Create Super BaseServiceImpl");
		this.crudRepository = crudRepository;
	}
	
	public T findById(ID id) {
		log.debug("BaseServiceImpl::findById");
		return crudRepository.findById(id).orElse(null);		
	}

	public T save(T entity) {
		log.debug("BaseServiceImpl::save");
		return crudRepository.save(entity);
	}

	public Set<T> findAll() {
		log.debug("BaseServiceImpl::findAll");
		Set<T> entities = new HashSet<>();
		crudRepository.findAll().forEach(entities::add);
		return entities;
	}

	public void delete(T entity) {
		log.debug("BaseServiceImpl::delete");
		crudRepository.delete(entity);
	}

	public void deleteById(ID id) {
		log.debug("BaseServiceImpl::deleteById");
		crudRepository.deleteById(id);
	}

}
