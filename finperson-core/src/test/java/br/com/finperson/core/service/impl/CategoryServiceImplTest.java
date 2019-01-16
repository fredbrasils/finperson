package br.com.finperson.core.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.finperson.core.repository.CategoryRepository;
import br.com.finperson.domain.CategoryEntity;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

	public static final String CATEGORY_NAME = "Transport";

	@Mock
	CategoryRepository categoryRepository;

	@InjectMocks
	CategoryServiceImpl categoryService;

	CategoryEntity returnCategory;

	@BeforeEach
	void setUp() {
		returnCategory = CategoryEntity.builder().id(1l).name(CATEGORY_NAME).build();
	}

	@Test
	void findAll() {
		Set<CategoryEntity> returnCategoriesSet = new HashSet<CategoryEntity>();
		returnCategoriesSet.add(CategoryEntity.builder().id(1l).build());
		returnCategoriesSet.add(CategoryEntity.builder().id(2l).build());

		when(categoryRepository.findAll()).thenReturn(returnCategoriesSet);

		Set<CategoryEntity> categories = categoryService.findAll();

		assertNotNull(categories);
		assertEquals(2, categories.size());
	}

	@Test
	void findById() {
		when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(returnCategory));

		CategoryEntity category = categoryService.findById(1L);

		assertNotNull(category);
	}

	@Test
	void findByIdNotFound() {
		when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

		CategoryEntity category = categoryService.findById(1L);

		assertNull(category);
	}

	@Test
	void save() {
		CategoryEntity categoryToSave = CategoryEntity.builder().id(1L).build();

		when(categoryRepository.save(any())).thenReturn(returnCategory);

		CategoryEntity savedCategory = categoryService.save(categoryToSave);

		assertNotNull(savedCategory);

		verify(categoryRepository).save(any());
	}

	@Test
	void delete() {
		categoryService.delete(returnCategory);

		// default is 1 times
		verify(categoryRepository, times(1)).delete(any());
	}

	@Test
	void deleteById() {
		categoryService.deleteById(1L);

		verify(categoryRepository).deleteById(anyLong());
	}
}
