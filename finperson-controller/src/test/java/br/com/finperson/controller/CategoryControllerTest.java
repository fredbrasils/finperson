package br.com.finperson.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import br.com.finperson.core.exception.EntityExistsException;
import br.com.finperson.core.service.CategoryService;
import br.com.finperson.core.service.UserService;
import br.com.finperson.model.CategoryEntity;
import br.com.finperson.model.UserEntity;
import br.com.finperson.util.ConstantsMessages;
@ExtendWith(MockitoExtension.class)
class CategoryControllerTest extends AbstractRestControllerTest{

	@Mock
    CategoryService categoryService;

	@Mock
    UserService userService;
	
    @InjectMocks
    CategoryController categoryController;

    @Mock
    MessageSource messages;
    
    List<CategoryEntity> categories;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        categories = new ArrayList<CategoryEntity>();
        categories.add(CategoryEntity.builder().id(1l).build());
        categories.add(CategoryEntity.builder().id(2l).build());

        mockMvc = MockMvcBuilders
                .standaloneSetup(categoryController)
                .build();
    }
    
    @DisplayName(value="Find all user's categories.") 
    @Test
    void findAllCategories() throws Exception {
		
    	UserEntity user = UserEntity.builder().id(1l)
    			.firstName("Fred")
    			.lastName("Brasil")
    			.email("fredbrasils@hotmail.com")
    			.build();

    	Optional<List<CategoryEntity>> categoryList = Optional.of(categories);
    	
    	when(categoryService.findAllByUser(ArgumentMatchers.any(UserEntity.class))).thenReturn(categoryList);
    	
    	Authentication authentication = Mockito.mock(Authentication.class);
    	SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    	Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
    	Mockito.when(authentication.getPrincipal()).thenReturn(user);
    	SecurityContextHolder.setContext(securityContext);
    	
        mockMvc.perform(get("/api/category/list")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
                ;

    }

    @DisplayName(value="Invalid user.") 
    @Test
    void notReturnCategoriesInvalidUser() throws Exception {
		
    	Authentication authentication = Mockito.mock(AnonymousAuthenticationToken.class);
    	SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    	Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
    	SecurityContextHolder.setContext(securityContext);
    	
    	when(messages.getMessage(anyString(),eq(null),ArgumentMatchers.any(Locale.class))).thenReturn("messages");

    	mockMvc.perform(get("/api/category/list")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", equalTo(false)))
                ;

        verify(categoryService, times(0)).findAll();
        verify(userService, times(0)).findByEmail(any());
    }
    
    @DisplayName(value="Authentication is null.") 
    @Test
    void notReturnCategoriesAuthenticationIsNull() throws Exception {
		
    	SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    	Mockito.when(securityContext.getAuthentication()).thenReturn(null);
    	SecurityContextHolder.setContext(securityContext);
    	
    	when(messages.getMessage(anyString(),eq(null),ArgumentMatchers.any(Locale.class))).thenReturn("messages");

    	mockMvc.perform(get("/api/category/list")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", equalTo(false)))
                ;

        verify(categoryService, times(0)).findAll();
        verify(userService, times(0)).findByEmail(any());
    }
    
    @DisplayName(value="Create a new category")
    @Test
    void registerCategory() throws Exception {
    	
    	CategoryEntity category = CategoryEntity.builder()
    			.name("home").icon("fas fa-plus").color("10-10-10-1")
    			.build();

    	when(categoryService.create(ArgumentMatchers.any(CategoryEntity.class))).thenReturn(category);

        mockMvc.perform(post("/api/category/insert")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(asJsonString(category)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.object.name", equalTo(category.getName())))
                .andExpect(jsonPath("$.object.icon", equalTo(category.getIcon())))
                .andExpect(jsonPath("$.object.color", equalTo(category.getColor())))
                ;

        verify(categoryService).create(ArgumentMatchers.any());

    }
    
    @DisplayName(value="Category already exists")
    @Test
    void categoryExists() throws Exception {
    	
    	CategoryEntity category = CategoryEntity.builder()
    			.name("home").icon("fas fa-plus").color("10-10-10-1")
    			.build();

    	when(categoryService.create(ArgumentMatchers.any(CategoryEntity.class))).thenThrow(EntityExistsException.class);

        mockMvc.perform(post("/api/category/insert")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(asJsonString(category)))
                .andExpect(status().isBadRequest())
                ;
        
        verify(categoryService).create(ArgumentMatchers.any());
    }
    
    @DisplayName(value="Category miss field")
    @Test
    void categoryMissField() throws Exception {
    	
    	CategoryEntity category = CategoryEntity.builder()
    			.icon("fas fa-plus").color("10-10-10-1")
    			.build();

        mockMvc.perform(post("/api/category/insert")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(asJsonString(category)))
                .andExpect(status().isBadRequest())
                ;
        
        verify(categoryService, times(0)).create(ArgumentMatchers.any());
    }
    
    @DisplayName(value="Update a category")
    @Test
    void updateCategory() throws Exception {
    	
    	CategoryEntity category = CategoryEntity.builder()
    			.name("home").icon("fas fa-plus").color("10-10-10-1")
    			.build();

    	when(categoryService.update(ArgumentMatchers.any(CategoryEntity.class))).thenReturn(category);

        mockMvc.perform(post("/api/category/update")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(asJsonString(category)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.object.name", equalTo(category.getName())))
                .andExpect(jsonPath("$.object.icon", equalTo(category.getIcon())))
                .andExpect(jsonPath("$.object.color", equalTo(category.getColor())))
                ;

        verify(categoryService).update(ArgumentMatchers.any());

    }
    
    @DisplayName(value="Doesnt update Category because category's name already exists")
    @Test
    void DontUpdateCategoryExists() throws Exception {
    	
    	CategoryEntity category = CategoryEntity.builder()
    			.name("home").icon("fas fa-plus").color("10-10-10-1")
    			.build();

    	when(categoryService.update(ArgumentMatchers.any(CategoryEntity.class))).thenThrow(EntityExistsException.class);

        mockMvc.perform(post("/api/category/update")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(asJsonString(category)))
                .andExpect(status().isBadRequest())
                ;
        
        verify(categoryService).update(ArgumentMatchers.any());
    }
    
    @DisplayName(value="Doesnt update Category because miss field")
    @Test
    void dontUpdateCategoryMissField() throws Exception {
    	
    	CategoryEntity category = CategoryEntity.builder()
    			.icon("fas fa-plus").color("10-10-10-1")
    			.build();

        mockMvc.perform(post("/api/category/update")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(asJsonString(category)))
                .andExpect(status().isBadRequest())
                ;
        
        verify(categoryService, times(0)).update(ArgumentMatchers.any());
    }
    
    @DisplayName(value="Remove a category")
    @Test
    void removeCategory() throws Exception {
    	
    	CategoryEntity category = CategoryEntity.builder()
    			.name("home").icon("fas fa-plus").color("10-10-10-1")
    			.build();

    	category.setId(1l);
    	
        mockMvc.perform(post("/api/category/delete")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(asJsonString(category)))
                .andExpect(status().isOk());

        verify(categoryService).delete(ArgumentMatchers.any());

    }
    
    @DisplayName(value="Dont remove a category")
    @Test
    void dontRemoveCategory() throws Exception {
    	
    	CategoryEntity category = CategoryEntity.builder()
    			.icon("fas fa-plus").color("10-10-10-1")
    			.build();

        mockMvc.perform(post("/api/category/delete")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(asJsonString(category)))
                .andExpect(status().isBadRequest());

        verify(categoryService, times(0)).delete(ArgumentMatchers.any());
    }
    
    /* SUBCATEGORY */
    
    @DisplayName(value="Create a new subcategory")
    @Test
    void registerSubCategory() throws Exception {
    	
    	CategoryEntity cat = CategoryEntity.builder().build();
    	cat.setId(1l);
    	
    	CategoryEntity category = CategoryEntity.builder()
    			.name("work").icon("fas fa-plus").color("10-10-10-1")
    			.mainCategory(cat)
    			.build();

    	when(categoryService.createSubCategory(ArgumentMatchers.any(CategoryEntity.class))).thenReturn(category);

        mockMvc.perform(post("/api/category/subcategory/insert")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(asJsonString(category)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.object.name", equalTo(category.getName())))
                .andExpect(jsonPath("$.object.icon", equalTo(category.getIcon())))
                .andExpect(jsonPath("$.object.color", equalTo(category.getColor())))
           		.andExpect(jsonPath("$.object.mainCategory.id", equalTo(1)));        
        
        verify(categoryService).createSubCategory(ArgumentMatchers.any());
    }
    
    @DisplayName(value="Dont create subcategory because miss field")
    @Test
    void dontCreateSubcategoryMissField() throws Exception {
    	
    	CategoryEntity cat = CategoryEntity.builder().build();
    	cat.setId(1l);
    	
    	CategoryEntity category = CategoryEntity.builder()
    			.icon("fas fa-plus").color("10-10-10-1")
    			.mainCategory(cat)
    			.build();

    	mockMvc.perform(post("/api/category/subcategory/insert")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(asJsonString(category)))
                .andExpect(status().isBadRequest())
                ;   
              
        verify(categoryService, times(0)).createSubCategory(ArgumentMatchers.any());
        
    }
    
    @DisplayName(value="Dont create subcategory because miss main category")
    @Test
    void dontCreateSubcategoryMissMainCategory() throws Exception {    	
   	
    	CategoryEntity category = CategoryEntity.builder().name("work")
    			.icon("fas fa-plus").color("10-10-10-1")
    			.build();

    	when(messages.getMessage(anyString(),eq(null),ArgumentMatchers.any(Locale.class))).thenReturn(ConstantsMessages.SUBCATEGORY_MESSAGE_ERROR_WITHOUT_MAINCATEGORY);
    	
    	mockMvc.perform(post("/api/category/subcategory/insert")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(asJsonString(category)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", org.hamcrest.Matchers.contains(ConstantsMessages.SUBCATEGORY_MESSAGE_ERROR_WITHOUT_MAINCATEGORY)))
                ;   
              
        verify(categoryService, times(0)).createSubCategory(ArgumentMatchers.any());        
    }
    
    @DisplayName(value="Dont create subcategory because subcategory already exists")
    @Test
    void dontCreateSubcategoryAlreadyExists() throws Exception {
    	
    	CategoryEntity cat = CategoryEntity.builder().build();
    	cat.setId(1l);
    	
    	CategoryEntity category = CategoryEntity.builder().name("work")
    			.icon("fas fa-plus").color("10-10-10-1")
    			.mainCategory(cat)
    			.build();

    	when(categoryService.createSubCategory(ArgumentMatchers.any(CategoryEntity.class))).thenThrow(EntityExistsException.class);
    	
    	mockMvc.perform(post("/api/category/subcategory/insert")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(asJsonString(category)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", equalTo(false)))
                ;   
              
        verify(categoryService, times(1)).createSubCategory(ArgumentMatchers.any());
        
    }
}
