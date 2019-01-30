package br.com.finperson.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;

import br.com.finperson.core.service.CategoryService;
import br.com.finperson.util.ConstantsURL;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class CategoryController {

	private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
    
    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }
    
    @GetMapping(value = ConstantsURL.SLASH + ConstantsURL.CATEGORY_LIST)
    public String findCategories(Model model){
    	log.debug("CategoryController::findCategories");
        model.addAttribute("categories", categoryService.findAll());
        return ConstantsURL.CATEGORY_CATEGORIES;
    }
}
