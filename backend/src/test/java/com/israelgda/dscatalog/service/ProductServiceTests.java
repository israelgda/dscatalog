package com.israelgda.dscatalog.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.israelgda.dscatalog.dto.ProductDTO;
import com.israelgda.dscatalog.entities.Category;
import com.israelgda.dscatalog.entities.Product;
import com.israelgda.dscatalog.repositories.CategoryRepository;
import com.israelgda.dscatalog.repositories.ProductRepository;
import com.israelgda.dscatalog.services.ProductService;
import com.israelgda.dscatalog.services.exceptions.DataBaseException;
import com.israelgda.dscatalog.services.exceptions.ResourceNotFoundException;
import com.israelgda.dscatalog.tests.Factory;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {
	
	@InjectMocks
	private ProductService service;
	
	@Mock
	private ProductRepository repository;
	
	@Mock
	private CategoryRepository categoryRepository;
	
	private long existingId;
	private long nonExistingId;
	private long associatedId;
	private PageImpl<Product> page;
	private Product product;
	private ProductDTO productDTO;
	private Category category;
	
	@BeforeEach
	public void setUp() {
		existingId = 1L;
		nonExistingId = 999L;
		associatedId = 4L;
		product = Factory.createProduct();
		productDTO = Factory.createProductDTO();
		category = Factory.createCategory();
		page = new PageImpl<>(List.of(product));
		
		Mockito.when(repository
				.findAll((Pageable)ArgumentMatchers.any()))
				.thenReturn(page);
		
		Mockito.when(repository
				.findById(existingId))
				.thenReturn(Optional.of(product));
		
		Mockito.when(repository
				.findById(nonExistingId))
				.thenReturn(Optional.empty());
		
		Mockito.when(repository
				.getOne(existingId))
				.thenReturn(product);
		
		Mockito.when(repository
				.getOne(nonExistingId))
				.thenThrow(EntityNotFoundException.class);
		
		Mockito.when(categoryRepository
				.getOne(existingId))
				.thenReturn(category);
		
		Mockito.when(categoryRepository
				.getOne(nonExistingId))
				.thenThrow(EntityNotFoundException.class);
		
		Mockito.when(repository
				.save(ArgumentMatchers.any()))
				.thenReturn(product);
		
		Mockito.doNothing()
				.when(repository)
				.deleteById(existingId);
		
		Mockito.doThrow(EmptyResultDataAccessException.class)
				.when(repository)
				.deleteById(nonExistingId);
		
		Mockito.doThrow(DataIntegrityViolationException.class)
				.when(repository)
				.deleteById(associatedId);
	}

	@Test
	public void findByIdShouldReturnProductDTOWhenIdExists() {
		ProductDTO productDTO = service.findById(existingId);
		
		assertNotNull(productDTO);
		
		Mockito.verify(repository, Mockito.times(1)).findById(existingId);
		
	}
	
	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
		
		assertThrows(ResourceNotFoundException.class, ()->{
			service.findById(nonExistingId);
		});
		
		Mockito.verify(repository, Mockito.times(1)).findById(nonExistingId);
		
	}
	
	@Test
	public void updateShouldReturnProductDTOWhenIdExists() {
		ProductDTO newProductDTO = service.update(existingId, productDTO);
		
		assertNotNull(newProductDTO);
		
		Mockito.verify(repository, Mockito.times(1)).getOne(existingId);
		Mockito.verify(repository, Mockito.times(1)).save(product);
	}
	
	@Test
	public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
		
		assertThrows(ResourceNotFoundException.class, ()->{
			service.update(nonExistingId, productDTO);
		});		
	}
	
	@Test
	public void findAllPagedShouldReturnPage() {
		Pageable pageable = PageRequest.of(0, 10);
		
		Page<ProductDTO> result = service.findAllPaged(pageable);
		
		Assertions.assertNotNull(result);
		Mockito.verify(repository, Mockito.times(1)).findAll(pageable);
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
		
		assertThrows(ResourceNotFoundException.class, ()-> {
			service.delete(nonExistingId);
		});
		
		Mockito.verify(repository, Mockito.times(1)).deleteById(nonExistingId);
	}
	
	@Test
	public void deleteShouldThrowDatabaseExceptionWhenAssociatedId() {
		
		assertThrows(DataBaseException.class, ()-> {
			service.delete(associatedId);
		});
		
		Mockito.verify(repository, Mockito.times(1)).deleteById(associatedId);
	}
	
	@Test
	public void deleteShouldDoNothingWhenIdExists() {
		
		assertDoesNotThrow(()-> {
			service.delete(existingId);
		});
		
		Mockito.verify(repository, Mockito.times(1)).deleteById(existingId);
	}
	
	
}
