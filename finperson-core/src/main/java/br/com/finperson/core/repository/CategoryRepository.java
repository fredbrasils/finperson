package br.com.finperson.core.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import br.com.finperson.model.CategoryEntity;

public interface CategoryRepository extends PagingAndSortingRepository<CategoryEntity, Long>{

	CategoryEntity findByNameIgnoreCase(String name);
	
}
