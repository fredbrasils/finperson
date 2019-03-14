package br.com.finperson.core.service;

import java.util.List;

import org.springframework.data.domain.Sort;

import br.com.finperson.core.exception.EntityExistsException;
import br.com.finperson.model.CategoryEntity;

public interface CategoryService extends BaseService<CategoryEntity, Long>{

	CategoryEntity create(CategoryEntity entity) throws EntityExistsException;
	
	List<CategoryEntity> findAll(Sort sort);
}
