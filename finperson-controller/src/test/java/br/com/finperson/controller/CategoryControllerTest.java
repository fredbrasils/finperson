package br.com.finperson.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import br.com.finperson.core.service.CategoryService;
import br.com.finperson.model.CategoryEntity;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

	@Mock
    CategoryService categoryService;

    @InjectMocks
    CategoryController categoryController;

    Set<CategoryEntity> categories;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        categories = new HashSet<CategoryEntity>();
        categories.add(CategoryEntity.builder().id(1l).build());
        categories.add(CategoryEntity.builder().id(2l).build());

        mockMvc = MockMvcBuilders
                .standaloneSetup(categoryController)
                .build();
    }

    @Test
    void findAllCategories() throws Exception {
    	
    	when(categoryService.findAll()).thenReturn(categories);
    	
        mockMvc.perform(get("/category/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("category/categories"))
                .andExpect(model().attributeExists("categories"))
                .andExpect(model().attribute("categories", hasSize(2)));;

    }

}
