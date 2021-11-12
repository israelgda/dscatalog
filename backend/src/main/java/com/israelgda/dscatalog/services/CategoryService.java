package com.israelgda.dscatalog.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.israelgda.dscatalog.dto.CategoryDTO;
import com.israelgda.dscatalog.entities.Category;
import com.israelgda.dscatalog.repositories.CategoryRepository;
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
	public List<CategoryDTO> findAll() {
		List<CategoryDTO> listDTO = listToDTO(repository.findAll());
		return listDTO;
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
	

	//Métodos Internos
	private Category dtoToEntity(CategoryDTO categoryDTO) {
		Category category = new Category();
		category.setId(categoryDTO.getId());
		category.setName(categoryDTO.getName());
		return category;
	}

	private List<CategoryDTO> listToDTO(List<Category> list) {
		List<CategoryDTO> result = list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());
		return result;
	}
	
	private Category updateCategory(CategoryDTO categoryDTO, Category newCategory) {
		newCategory.setName(categoryDTO.getName());
		return newCategory;
	}

}
