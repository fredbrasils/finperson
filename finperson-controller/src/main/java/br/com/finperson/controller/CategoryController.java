package br.com.finperson.controller;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.finperson.core.service.CategoryService;
import br.com.finperson.core.service.UserService;
import br.com.finperson.model.CategoryEntity;
import br.com.finperson.model.UserEntity;
import br.com.finperson.util.ConstantsMessages;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/category")
public class CategoryController extends BaseController{

	private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService, UserService userService) {
    	super(userService);
        this.categoryService = categoryService;
    }
    
    @GetMapping(value = "/list")
	public ResponseEntity<?> findCategories(HttpServletRequest request) {
	  
    	log.debug("CategoryController:findCategories");
    	UserEntity user = getCurrentUser();
	    if (user == null) {
	    	return messageError(request, new String[] {ConstantsMessages.INVALID_USER}, null);
	    }
	     
	    Set<CategoryEntity> list = categoryService.findAll();
	    
        return ResponseEntity.ok(list);
    
    }
    
}
