package br.com.finperson.core.service.impl;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import br.com.finperson.core.exception.EntityExistsException;
import br.com.finperson.core.repository.CategoryRepository;
import br.com.finperson.core.service.CategoryService;
import br.com.finperson.model.CategoryEntity;
import br.com.finperson.model.UserEntity;
import br.com.finperson.util.ConstantsMessages;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CategoryServiceImpl extends BaseServiceImpl<CategoryEntity,Long> implements CategoryService{
	
	private CategoryRepository categoryRepository;	
	
	public CategoryServiceImpl(CategoryRepository categoryRepository) {
		super(categoryRepository);
		this.categoryRepository = categoryRepository;
	}	

	@Transactional
    @Override
	public CategoryEntity create(CategoryEntity entity) throws EntityExistsException {
		
		log.debug("Create category: ",entity.getName());
		
		Optional<CategoryEntity> categorySearched = categoryRepository.findOptionalByUserAndNameIgnoreCase(entity.getUser(),entity.getName());
		
		if(categorySearched.isPresent()) {
			throw new EntityExistsException(ConstantsMessages.CATEGORY_MESSAGE_ERROR_EXISTS);
		}
		
		entity.setName(StringUtils.capitalize(entity.getName())); 
		return categoryRepository.save(entity);
	}

	@Transactional
    @Override
	public CategoryEntity update(CategoryEntity entity) throws EntityExistsException {
		
		log.debug("Update category");
		
		Optional<CategoryEntity> categorySearched = categoryRepository.findOptionalByUserAndNameIgnoreCase(entity.getUser(),entity.getName());
		
		if(categorySearched.isPresent() && !categorySearched.get().getId().equals(entity.getId())) {
			throw new EntityExistsException(ConstantsMessages.CATEGORY_MESSAGE_ERROR_EXISTS);
		}
		
		entity.setName(StringUtils.capitalize(entity.getName())); 
		return categoryRepository.save(entity);
	}
	
	@Override
	public Optional<List<CategoryEntity>> findAllByUser(UserEntity user) {
		return categoryRepository.findOptionalByUserOrderByNameAsc(user);
	}

	@Override
	public void delete(CategoryEntity entity) {

		//TODO: verify if category have relation avec revenue
		super.delete(entity);
		
	}

	@Override
	public Optional<CategoryEntity> findByIdAndUser(Long id, UserEntity user) {
		return categoryRepository.findOptionalByIdAndUser(id,user);
	}

	@Override
	public Optional<CategoryEntity> findByNameAndUser(String name, UserEntity user) {
		return categoryRepository.findOptionalByNameAndUser(name,user);
	}
	
	@Transactional
    @Override
	public CategoryEntity createSubCategory(CategoryEntity entity) throws EntityExistsException {
		
		log.debug("Create subcategory: ",entity.getName());
		
		Optional<CategoryEntity> categorySearched 
			= categoryRepository.findOptionalByUserAndMainCategoryAndNameIgnoreCase(entity.getUser(), entity.getMainCategory(), entity.getName());
		
		if(categorySearched.isPresent()) {
			throw new EntityExistsException(ConstantsMessages.CATEGORY_MESSAGE_ERROR_EXISTS);
		}
		
		entity.setName(StringUtils.capitalize(entity.getName())); 
		return categoryRepository.save(entity);
	}
}
