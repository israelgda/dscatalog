package com.israelgda.dscatalog.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.israelgda.dscatalog.dto.CategoryDTO;
import com.israelgda.dscatalog.entities.Category;
import com.israelgda.dscatalog.repositories.CategoryRepository;

@Service
public class CategoryService {
	
	private final CategoryRepository repository;
	
	public CategoryService(CategoryRepository repository) {
		this.repository = repository;
	}
	
	@Transactional(readOnly = true)
	public List<CategoryDTO> findAll(){
		List<CategoryDTO> listDTO = listToDTO(repository.findAll());
		return listDTO;
	}

	private List<CategoryDTO> listToDTO(List<Category> list) {
		List<CategoryDTO> result = list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());
		return result;
	}
}
