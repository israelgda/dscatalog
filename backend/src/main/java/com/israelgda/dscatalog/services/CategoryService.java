package com.israelgda.dscatalog.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.israelgda.dscatalog.entities.Category;
import com.israelgda.dscatalog.repositories.CategoryRepository;

@Service
public class CategoryService {
	
	private final CategoryRepository repository;
	
	public CategoryService(CategoryRepository repository) {
		this.repository = repository;
	}
	
	public List<Category> findAll(){
		return repository.findAll();
	}
}
