package com.israelgda.dscatalog.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.israelgda.dscatalog.entities.Product;
import com.israelgda.dscatalog.tests.Factory;

@DataJpaTest
public class ProductRepositoryTests {
	
	@Autowired
	private ProductRepository repository;
	
	private long existingId;
	private long notExistingId;
	private long countTotalProducts;
	
	
	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		notExistingId = 987L;
		countTotalProducts = 25L;
	}
	
	@Test
	public void findShouldReturnOptionalNotNullWhenIdExists() {
		Optional<Product> product = repository.findById(existingId);
		
		assertTrue(product.isPresent());
	}
	
	@Test
	public void findShouldReturnOptionalNullWhenIdDoesNotexists() {
		Optional<Product> product = repository.findById(notExistingId);
		
		assertFalse(product.isPresent());
	}
	
	@Test
	public void saveShouldPersistWithAutoincrementWhenIdIsNull() {
		Product product = Factory.createProduct();
		product.setId(null);
		
		product = repository.save(product);
		
		assertNotNull(product.getId());
		assertEquals(countTotalProducts + 1, product.getId());
		
	}
	
	@Test
	public void deleteShouldDeleteObjectWhenIdExists() {
		repository.deleteById(existingId);
		
		Optional<Product> result = repository.findById(existingId);
		assertFalse(result.isPresent());
	}
	
	@Test
	public void deleteShouldThrowExceptionWhenIdDoesNotExists() {
		
		assertThrows(EmptyResultDataAccessException.class, ()-> {
			repository.deleteById(notExistingId);
		});
	}
}
