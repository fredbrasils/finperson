package br.com.finperson.core.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;

import br.com.finperson.model.CategoryEntity;
import br.com.finperson.model.UserEntity;

public interface CategoryRepository extends PagingAndSortingRepository<CategoryEntity, Long> {

	Optional<CategoryEntity> findOptionalByUserAndNameIgnoreCase(UserEntity user, String name);

	Optional<List<CategoryEntity>> findOptionalByUserOrderByNameAsc(UserEntity user);

	Optional<CategoryEntity> findOptionalByIdAndUser(Long id, UserEntity user);

	Optional<CategoryEntity> findOptionalByNameAndUser(String name, UserEntity user);

	Optional<CategoryEntity> findOptionalByUserAndMainCategoryAndNameIgnoreCase(UserEntity user, CategoryEntity mainCategory,
			String name);
}
