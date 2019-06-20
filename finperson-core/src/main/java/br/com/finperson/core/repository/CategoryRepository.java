package br.com.finperson.core.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import br.com.finperson.model.CategoryEntity;
import br.com.finperson.model.UserEntity;

public interface CategoryRepository extends PagingAndSortingRepository<CategoryEntity, Long> {

	Optional<List<CategoryEntity>> findOptionalByUserAndNameIgnoreCase(UserEntity user, String name);

	@Query("select distinct c from CategoryEntity c inner join fetch c.user u left join fetch c.subCategories sub where u = :user and c.mainCategory is null order by c.name, sub.name")
	List<CategoryEntity> findAllByUser(@Param("user") UserEntity user);
	
	Optional<List<CategoryEntity>> findOptionalByIdAndUser(Long id, UserEntity user);

	Optional<List<CategoryEntity>> findOptionalByNameAndUser(String name, UserEntity user);

	Optional<CategoryEntity> findOptionalByUserAndMainCategoryAndNameIgnoreCase(UserEntity user, CategoryEntity mainCategory,
			String name);
}
