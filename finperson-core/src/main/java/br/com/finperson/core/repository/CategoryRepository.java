package br.com.finperson.core.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;

import br.com.finperson.model.CategoryEntity;
import br.com.finperson.model.UserEntity;

public interface CategoryRepository extends PagingAndSortingRepository<CategoryEntity, Long>{

	Optional<CategoryEntity> findOptionalByUserAndNameIgnoreCase(UserEntity user, String name);
	Optional<List<CategoryEntity>> findByUserOrderByNameAsc(UserEntity user);
	Optional<CategoryEntity> findByIdAndUser(Long id, UserEntity user);
}
