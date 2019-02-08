package br.com.finperson.core.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.finperson.model.CategoryEntity;

public interface CategoryRepository extends CrudRepository<CategoryEntity, Long>{

}
