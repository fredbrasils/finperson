package br.com.finperson.core.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.google.common.collect.Lists;

import br.com.finperson.core.exception.EntityExistsException;
import br.com.finperson.core.repository.CategoryRepository;
import br.com.finperson.core.service.CategoryService;
import br.com.finperson.model.CategoryEntity;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CategoryServiceImpl extends BaseServiceImpl<CategoryEntity,Long> implements CategoryService{
	
	private CategoryRepository categoryRepository;	
	
	public CategoryServiceImpl(CategoryRepository categoryRepository) {
		super(categoryRepository);
		log.debug("Create CategoryServiceImpl");
		this.categoryRepository = categoryRepository;
	}	

	@Transactional
    @Override
	public CategoryEntity create(CategoryEntity entity) throws EntityExistsException {
		
		log.debug("Create category: ",entity.getName());
		
		CategoryEntity categorySearched = categoryRepository.findByNameIgnoreCase(entity.getName());
		
		if(categorySearched != null) {
			throw new EntityExistsException("");
		}
		
		entity.setName(StringUtils.capitalize(entity.getName())); 
		return categoryRepository.save(entity);
	}

	@Transactional
    @Override
	public CategoryEntity update(CategoryEntity entity) throws EntityExistsException {
		
		log.debug("Update category");
		
		CategoryEntity categorySearched = categoryRepository.findByNameIgnoreCase(entity.getName());
		
		if(categorySearched != null && !categorySearched.getId().equals(entity.getId())) {
			throw new EntityExistsException("");
		}
		
		entity.setName(StringUtils.capitalize(entity.getName())); 
		return categoryRepository.save(entity);
	}
	
	@Override
	public List<CategoryEntity> findAllByOrderByName() {
		return Lists.newArrayList(categoryRepository.findAll(orderBy("name")));
	}

	@Override
	public void delete(CategoryEntity entity) {

		//TODO: verify if category have relation avec revenue
		super.delete(entity);
		
	}
	
	private Sort orderBy(String field) {
		return Sort.by(Sort.Order.asc(field).ignoreCase());
	}
}
