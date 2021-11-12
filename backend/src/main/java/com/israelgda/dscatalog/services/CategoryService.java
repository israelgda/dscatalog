package com.israelgda.dscatalog.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.israelgda.dscatalog.dto.CategoryDTO;
import com.israelgda.dscatalog.entities.Category;
import com.israelgda.dscatalog.repositories.CategoryRepository;
import com.israelgda.dscatalog.services.exceptions.DataBaseException;
import com.israelgda.dscatalog.services.exceptions.ResourceNotFoundException;

@Service
public class CategoryService {

	private final CategoryRepository repository;

	public CategoryService(CategoryRepository repository) {
		this.repository = repository;
	}

	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id) {
		Optional<Category> category = repository.findById(id);
		CategoryDTO result = new CategoryDTO(category.orElseThrow(()-> new ResourceNotFoundException("Entity not found. Id: " + id)));
		return result;
	}

	@Transactional(readOnly = true)
	public Page<CategoryDTO> findAllPaged(PageRequest pageRequest) {
		Page<Category> listDTO = repository.findAll(pageRequest);
		return listDTO.map(x-> new CategoryDTO(x));
	}

	@Transactional
	public CategoryDTO create(CategoryDTO categoryDTO) {
		Category category = dtoToEntity(categoryDTO);
		category = repository.save(category);
		CategoryDTO result = new CategoryDTO(category);
		return result;
	}
	
	@Transactional
	public CategoryDTO update(Long id, CategoryDTO categoryDTO) {
		try {
			Category newCategory = repository.getOne(id);
			repository.save(updateCategory(categoryDTO, newCategory));
			return new CategoryDTO(newCategory);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Entity not found. Id: " + id);
		}
	}
	
	public void delete(Long id) {
		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Entity not found. Id: " + id);
		} catch (DataIntegrityViolationException e) {
			throw new DataBaseException("Data Integrity Violation. Delete denied");
		}
	}

	//Métodos Internos
	private Category dtoToEntity(CategoryDTO categoryDTO) {
		Category category = new Category();
		category.setId(categoryDTO.getId());
		category.setName(categoryDTO.getName());
		return category;
	}
	
	private Category updateCategory(CategoryDTO categoryDTO, Category newCategory) {
		newCategory.setName(categoryDTO.getName());
		return newCategory;
	}

}
