package br.com.finperson.core.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import br.com.finperson.core.exception.EntityExistsException;
import br.com.finperson.core.repository.CategoryRepository;
import br.com.finperson.model.CategoryEntity;

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
		returnCategory = CategoryEntity.builder().name(CATEGORY_NAME).color("10-10-10-1").icon("fas fa-plus").build();
		returnCategory.setId(1l);
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
	
	@Test
	void create() {
		
		CategoryEntity categoryToSave = CategoryEntity.builder().id(1L).build();

		when(categoryRepository.save(any())).thenReturn(categoryToSave);

		CategoryEntity savedCategory = null;
		try {
			savedCategory = categoryService.create(categoryToSave);
		} catch (EntityExistsException e) {
		}

		assertNotNull(savedCategory);

		verify(categoryRepository).save(any());
	}
	
	@DisplayName("Doesn't create because category exists")
	@Test
	void dontCreateCategoryExists() {
		
		CategoryEntity categoryToSave = CategoryEntity.builder().id(1L).build();

		when(categoryRepository.findByNameIgnoreCase(any())).thenReturn(returnCategory);

		CategoryEntity savedCategory = null;
		try {
			savedCategory = categoryService.create(categoryToSave);
		} catch (EntityExistsException e) {
			assertTrue(true);
		}

		assertNull(savedCategory);

		verify(categoryRepository, times(0)).save(any());
	}
	
	@Test
	void findAllOrderByName() {
		Set<CategoryEntity> returnCategoriesSet = new HashSet<CategoryEntity>();
		returnCategoriesSet.add(CategoryEntity.builder().id(1l).name("a").build());
		returnCategoriesSet.add(CategoryEntity.builder().id(2l).name("b").build());

		when(categoryRepository.findAll(any(Sort.class))).thenReturn(returnCategoriesSet);

		List<CategoryEntity> categories = categoryService.findAllByOrderByName();

		assertNotNull(categories);
		assertEquals(2, categories.size());
	}
	
	@Test
	void update() {
		
		CategoryEntity categoryToSave = CategoryEntity.builder().name("transport").build();
		categoryToSave.setId(1l);
		when(categoryRepository.save(any())).thenReturn(categoryToSave);
		when(categoryRepository.findByNameIgnoreCase(any())).thenReturn(returnCategory);
		
		CategoryEntity savedCategory = null;
		try {
			savedCategory = categoryService.update(categoryToSave);
		} catch (EntityExistsException e) {
		}

		assertNotNull(savedCategory);

		verify(categoryRepository).save(any());
	}
	
	@Test
	void dontUpdate() {
		
		CategoryEntity categoryToSave = CategoryEntity.builder().name("transport").build();
		categoryToSave.setId(2l);
		when(categoryRepository.findByNameIgnoreCase(any())).thenReturn(returnCategory);
		
		try {
			categoryService.update(categoryToSave);
		} catch (EntityExistsException e) {
			assertTrue(true);
		}


		verify(categoryRepository, times(0)).save(any());
	}
	
}
