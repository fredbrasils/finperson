package br.com.finperson.core.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

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
		
		if(existCategory(entity)) {
			throw new EntityExistsException(ConstantsMessages.CATEGORY_MESSAGE_ERROR_EXISTS);
		}
		
		entity.setName(StringUtils.capitalize(entity.getName())); 
		return categoryRepository.save(entity);
	}

	@Transactional
    @Override
	public CategoryEntity update(CategoryEntity entity) throws EntityExistsException {
		
		log.debug("Update category");
		
		if(existCategory(entity)) {
			throw new EntityExistsException(ConstantsMessages.CATEGORY_MESSAGE_ERROR_EXISTS);
		}
		
		entity.setName(StringUtils.capitalize(entity.getName())); 
		return categoryRepository.save(entity);
	}
	
	@Override
	public List<CategoryEntity> findAllByUser(UserEntity user) {
		return categoryRepository.findAllByUser(user);
	}

	@Override
	public void delete(CategoryEntity entity) {

		//TODO: verify if category have relation avec revenue
		super.delete(entity);
		
	}

	@Override
	public Optional<List<CategoryEntity>> findByIdAndUser(Long id, UserEntity user) {
		return categoryRepository.findOptionalByIdAndUser(id,user);
	}

	@Override
	public Optional<List<CategoryEntity>> findByNameAndUser(String name, UserEntity user) {
		return categoryRepository.findOptionalByNameAndUser(name,user);
	}
	
	@Transactional
    @Override
	public CategoryEntity createSubCategory(CategoryEntity entity) throws EntityExistsException {
		
		log.debug("Create subcategory: ",entity.getName());
		
		Optional<CategoryEntity> categorySearched 
			= categoryRepository.findOptionalByUserAndMainCategoryAndNameIgnoreCase(entity.getUser(), entity.getMainCategory(), entity.getName());
		
		if(categorySearched.isPresent() || entity.getName().equals(entity.getMainCategory().getName()) ) {
			throw new EntityExistsException(ConstantsMessages.CATEGORY_MESSAGE_ERROR_EXISTS);
		}
		
		entity.setName(StringUtils.capitalize(entity.getName())); 
		return categoryRepository.save(entity);
	}
	
	private boolean existCategory(CategoryEntity entity) {
		
		boolean valid = false;
		Optional<List<CategoryEntity>> categorySearched = categoryRepository.findOptionalByUserAndNameIgnoreCase(entity.getUser(),entity.getName());
		
		if(categorySearched.isPresent()) {
			
			Predicate<CategoryEntity> predicate = e -> 
							(!e.getId().equals(entity.getId()) && equals(e.getMainCategory(), entity.getMainCategory()))
							|| (e.getId().equals(entity.getId()) && !equals(e.getMainCategory(), entity.getMainCategory()))
							|| (!e.getId().equals(entity.getId()) && equalsMainCategoryName(e,entity)  )
							;
			
			valid = categorySearched.get().stream().anyMatch(predicate);
		}
		
		return valid;
	}
	
	private boolean equals(CategoryEntity cat1, CategoryEntity cat2) {
		
		if(cat1 == null &&  cat2 == null) {
			return true;
		}
		
		if( (cat1 == null &&  cat2 != null) || (cat1 != null &&  cat2 == null)) {
			return false;
		}
		
		return cat1.getId().equals(cat2.getId());
	}
	
	private boolean equalsMainCategoryName(CategoryEntity cat1, CategoryEntity cat2) {
		
		if(cat1.getMainCategory() == null &&  cat2.getMainCategory() != null
				&& cat1.getName().equals(cat2.getMainCategory().getName())) {
			return true;
		}else if(cat2.getMainCategory() == null &&  cat1.getMainCategory() != null
				&& cat2.getName().equals(cat1.getMainCategory().getName())) {
			return true;
		}
				
		return false;
	}
}
