package br.com.finperson.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.finperson.core.service.CategoryService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/category")
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
    
    @RequestMapping("/list")
    public String findCategories(Model model){
    	log.debug("CategoryController::findCategories");
        model.addAttribute("categories", categoryService.findAll());
        return "category/categories";
    }
}
