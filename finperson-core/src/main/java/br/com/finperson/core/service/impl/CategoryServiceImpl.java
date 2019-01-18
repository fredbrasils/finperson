package br.com.finperson.core.service.impl;

import org.springframework.stereotype.Service;

import br.com.finperson.core.repository.CategoryRepository;
import br.com.finperson.core.service.CategoryService;
import br.com.finperson.domain.CategoryEntity;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CategoryServiceImpl extends BaseServiceImpl<CategoryEntity,Long> implements CategoryService{
	
	private CategoryRepository categoryRepository;	
	
	public CategoryServiceImpl(CategoryRepository categoryRepository) {
		super(categoryRepository);
		//log.debug("Create CategoryServiceImpl");
		this.categoryRepository = categoryRepository;
	}	
	

}
