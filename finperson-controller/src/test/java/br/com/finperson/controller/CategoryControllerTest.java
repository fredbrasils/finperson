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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import br.com.finperson.core.service.CategoryService;
import br.com.finperson.core.service.UserService;
import br.com.finperson.model.CategoryEntity;
import br.com.finperson.model.UserEntity;
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
    
    @DisplayName(value="Find all user's categories.") 
    @Test
    void findAllCategories() throws Exception {
		
    	UserEntity user = UserEntity.builder().id(1l)
    			.firstName("Fred")
    			.lastName("Brasil")
    			.email("fredbrasils@hotmail.com")
    			.build();

    	when(userService.findByEmail(any())).thenReturn(user);
    	
    	when(categoryService.findAll()).thenReturn(categories);
    	
    	Authentication authentication = Mockito.mock(Authentication.class);
    	SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    	UserDetails userDetails = Mockito.mock(UserDetails.class);
    	Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
    	Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
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
    
}
