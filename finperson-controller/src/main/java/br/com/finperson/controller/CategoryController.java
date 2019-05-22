package br.com.finperson.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.finperson.core.exception.EntityExistsException;
import br.com.finperson.core.service.CategoryService;
import br.com.finperson.model.CategoryEntity;
import br.com.finperson.model.UserEntity;
import br.com.finperson.model.payload.GenericResponse;
import br.com.finperson.util.ConstantsMessages;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/category")
public class CategoryController extends BaseController{

	private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
    	super();
        this.categoryService = categoryService;
    }
    
    @GetMapping(value = "/list")
	public ResponseEntity<?> findCategories(HttpServletRequest request) {
	  
    	log.debug("CategoryController:findCategories");
    	UserEntity user = getCurrentUser();
	    if (user == null) {
	    	return messageError(request, new String[] {ConstantsMessages.INVALID_USER}, null);
	    }
	     
	    List<CategoryEntity> list = categoryService.findAllByUser(user).get();
	    
        return ResponseEntity.ok(list);
    
    }
    
    @PostMapping("/insert")
    public ResponseEntity<GenericResponse> createCategory(@Valid @RequestBody CategoryEntity category, BindingResult result,
			HttpServletRequest request, Errors errors) {
    	CategoryEntity categorySaved = null;
    	
    	if (!result.hasErrors()) {
    	
    		try {
    			categorySaved = categoryService.create(category);
			} catch (EntityExistsException e) {
				return messageError(request, new String[] {e.getMessage()}, null);
			}
    	}else {
			return messageError(request, validateErrors(result), null);
		} 

    	return ResponseEntity.ok(messageSuccess(categorySaved, request, new String[] {ConstantsMessages.SUCCESS}, null));
    }
    
    @PostMapping("/update")
    public ResponseEntity<GenericResponse> updateCategory(@Valid @RequestBody CategoryEntity category, BindingResult result,
			HttpServletRequest request, Errors errors) {
    	
    	CategoryEntity categorySaved = null;
    	
    	if (!result.hasErrors()) {
    	
    		try {
    			categorySaved = categoryService.update(category);
			} catch (EntityExistsException e) {
				return messageError(request, new String[] {e.getMessage()}, null);
			}
    	}else {
			return messageError(request, validateErrors(result), null);
		} 

    	return ResponseEntity.ok(messageSuccess(categorySaved, request, new String[] {ConstantsMessages.SUCCESS}, null));
    }
    
    @PostMapping("/delete")
    public ResponseEntity<GenericResponse> deleteCategory(@Valid @RequestBody CategoryEntity category, BindingResult result,
			HttpServletRequest request, Errors errors) {
    	
    	if (!result.hasErrors()) {
    		categoryService.delete(category);
    	}else {
			return messageError(request, validateErrors(result), null);
		} 

    	return ResponseEntity.ok(messageSuccess(null, request, new String[] {ConstantsMessages.SUCCESS}, null));
    }
}
