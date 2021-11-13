package com.israelgda.dscatalog.resources;

import java.net.URI;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.israelgda.dscatalog.dto.ProductDTO;
import com.israelgda.dscatalog.services.ProductService;

@RestController
@RequestMapping(value="/products")
public class ProductResource {
	
	private final ProductService service;
	
	public ProductResource(ProductService service) {
		this.service = service;
	}
	
	@GetMapping(value="/{id}")
	public ResponseEntity<ProductDTO> findById(@PathVariable Long id){
		ProductDTO product = service.findById(id);
		return ResponseEntity.ok().body(product);
	}
	
	@GetMapping
	public ResponseEntity<Page<ProductDTO>> findAll(
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "linesPerPage", defaultValue = "12") Integer linesPerPage,
			@RequestParam(value = "direction", defaultValue = "ASC") String direction,
			@RequestParam(value = "orderBy", defaultValue = "name") String orderBy
			){
		
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
				
		Page<ProductDTO> list = service.findAllPaged(pageRequest);
		return ResponseEntity.ok().body(list);
	}
	
	@PostMapping
	public ResponseEntity<ProductDTO> create(@RequestBody ProductDTO product){
		product = service.create(product);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(product.getId())
				.toUri();
		return ResponseEntity.created(uri).body(product);
	}
	
	@PutMapping(value="/{id}")
	public ResponseEntity<ProductDTO> create(@PathVariable Long id, @RequestBody ProductDTO productDTO){
		ProductDTO newProduct = service.update(id, productDTO);
		return ResponseEntity.ok().body(newProduct);
	}
	
	@DeleteMapping(value="/{id}")
	public ResponseEntity<ProductDTO> delete(@PathVariable Long id){
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
