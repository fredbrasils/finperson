package br.com.finperson.core.service;

import java.util.List;

import br.com.finperson.core.exception.EntityExistsException;
import br.com.finperson.model.CategoryEntity;

public interface CategoryService extends BaseService<CategoryEntity, Long>{

	CategoryEntity create(CategoryEntity entity) throws EntityExistsException;
	
	CategoryEntity update(CategoryEntity entity) throws EntityExistsException;
	
	List<CategoryEntity> findAllByOrderByName();
}
