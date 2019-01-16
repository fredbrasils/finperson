package br.com.finperson.core.service.impl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import br.com.finperson.core.repository.BaseRepository;
import br.com.finperson.core.service.BaseService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public abstract class BaseServiceImpl<T, ID> implements BaseService<T, ID>{
	
	private final BaseRepository<T, ID> baseRepository;
	
	public BaseServiceImpl(BaseRepository<T, ID> baseRepository) {
		super();
		log.debug("Create Super BaseServiceImpl");
		this.baseRepository = baseRepository;
	}
	
	public T findById(ID id) {
		log.debug("BaseServiceImpl::findById");
		return (T) baseRepository.findById(id).orElse(null);		
	}

	public T save(T entity) {
		log.debug("BaseServiceImpl::save");
		return (T) baseRepository.save(entity);
	}

	public Set<T> findAll() {
		log.debug("BaseServiceImpl::findAll");
		Set<T> entities = new HashSet<T>();
		baseRepository.findAll().forEach(entities::add);
		return entities;
	}

	public void delete(T entity) {
		log.debug("BaseServiceImpl::delete");
		baseRepository.delete(entity);
	}

	public void deleteById(ID id) {
		log.debug("BaseServiceImpl::deleteById");
		baseRepository.deleteById(id);
	}

}
