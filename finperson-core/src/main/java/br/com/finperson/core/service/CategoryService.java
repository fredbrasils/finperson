package br.com.finperson.core.service;

import java.util.List;
import java.util.Optional;

import br.com.finperson.core.exception.EntityExistsException;
import br.com.finperson.model.CategoryEntity;
import br.com.finperson.model.UserEntity;

public interface CategoryService extends BaseService<CategoryEntity, Long>{

	CategoryEntity create(CategoryEntity entity) throws EntityExistsException;
	
	CategoryEntity update(CategoryEntity entity) throws EntityExistsException;
	
	Optional<List<CategoryEntity>> findAllByUser(UserEntity user);

	Optional<CategoryEntity> findByIdAndUser(Long id, UserEntity user);
}
