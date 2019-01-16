package br.com.finperson.core.service.impl;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.finperson.core.repository.CategoryRepository;
import br.com.finperson.core.service.CategoryService;
import br.com.finperson.domain.CategoryEntity;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

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

}
