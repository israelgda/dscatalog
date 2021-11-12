package com.israelgda.dscatalog.resources;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.israelgda.dscatalog.dto.CategoryDTO;
import com.israelgda.dscatalog.services.CategoryService;

@RestController
@RequestMapping(value="/categories")
public class CategoryResource {
	
	private final CategoryService service;
	
	public CategoryResource(CategoryService service) {
		this.service = service;
	}
	
	@GetMapping(value="/{id}")
	public ResponseEntity<CategoryDTO> findById(@PathVariable Long id){
		CategoryDTO category = service.findById(id);
		return ResponseEntity.ok().body(category);
	}
	
	@GetMapping
	public ResponseEntity<List<CategoryDTO>> findAll(){
		List<CategoryDTO> list = service.findAll();
		return ResponseEntity.ok().body(list);
	}
	
	@PostMapping
	public ResponseEntity<CategoryDTO> create(@RequestBody CategoryDTO category){
		category = service.create(category);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(category.getId())
				.toUri();
		return ResponseEntity.created(uri).body(category);
	}
	
	@PutMapping(value="/{id}")
	public ResponseEntity<CategoryDTO> create(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO){
		CategoryDTO newCategory = service.update(id, categoryDTO);
		return ResponseEntity.ok().body(newCategory);
	}
	
	@DeleteMapping(value="/{id}")
	public ResponseEntity<CategoryDTO> delete(@PathVariable Long id){
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
