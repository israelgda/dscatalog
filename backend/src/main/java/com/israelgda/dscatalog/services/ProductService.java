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
import com.israelgda.dscatalog.dto.ProductDTO;
import com.israelgda.dscatalog.entities.Category;
import com.israelgda.dscatalog.entities.Product;
import com.israelgda.dscatalog.repositories.CategoryRepository;
import com.israelgda.dscatalog.repositories.ProductRepository;
import com.israelgda.dscatalog.services.exceptions.DataBaseException;
import com.israelgda.dscatalog.services.exceptions.ResourceNotFoundException;

@Service
public class ProductService {

	private final ProductRepository repository;
	
	private final CategoryRepository categoryRepository;

	public ProductService(ProductRepository repository, CategoryRepository categoryRepository) {
		this.repository = repository;
		this.categoryRepository = categoryRepository;
	}

	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		Optional<Product> product = repository.findById(id);
		Product result = product.orElseThrow(()-> new ResourceNotFoundException("Entity not found. Id: " + id));
		return new ProductDTO(result, result.getCategories());
	}

	@Transactional(readOnly = true)
	public Page<ProductDTO> findAllPaged(PageRequest pageRequest) {
		Page<Product> listDTO = repository.findAll(pageRequest);
		return listDTO.map(x-> new ProductDTO(x));
	}

	@Transactional
	public ProductDTO create(ProductDTO productDTO) {
		Product product = dtoToEntity(productDTO);
		product = repository.save(product);
		ProductDTO result = new ProductDTO(product, product.getCategories());
		return result;
	}
	
	@Transactional
	public ProductDTO update(Long id, ProductDTO productDTO) {
		try {
			Product newProduct = repository.getOne(id);
			repository.save(updateProduct(productDTO, newProduct));
			return new ProductDTO(newProduct);
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
	private Product dtoToEntity(ProductDTO productDTO) {
		Product product = new Product();
		product.setName(productDTO.getName());
		product.setDescription(productDTO.getDescription());
		product.setPrice(productDTO.getPrice());
		product.setImgUrl(productDTO.getImgUrl());
		product.setDate(productDTO.getDate());
		
		product.getCategories().clear();
		for(CategoryDTO catDto: productDTO.getCategories()) {
			Category category = categoryRepository.getOne(catDto.getId());
			product.getCategories().add(category);
		}
		
		return product;
	}
	
	private Product updateProduct(ProductDTO productDTO, Product newProduct) {
		newProduct.setName(productDTO.getName());
		newProduct.setDescription(productDTO.getDescription());
		newProduct.setPrice(productDTO.getPrice());
		newProduct.setImgUrl(productDTO.getImgUrl());
		newProduct.setDate(productDTO.getDate());
		
		newProduct.getCategories().clear();
		for(CategoryDTO catDto: productDTO.getCategories()) {
			Category category = categoryRepository.getOne(catDto.getId());
			newProduct.getCategories().add(category);
		}
		
		return newProduct;
	}

}
