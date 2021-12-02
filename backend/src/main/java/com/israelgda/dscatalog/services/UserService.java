package com.israelgda.dscatalog.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.israelgda.dscatalog.dto.RoleDTO;
import com.israelgda.dscatalog.dto.UserDTO;
import com.israelgda.dscatalog.dto.UserInsertDTO;
import com.israelgda.dscatalog.entities.Role;
import com.israelgda.dscatalog.entities.User;
import com.israelgda.dscatalog.repositories.CategoryRepository;
import com.israelgda.dscatalog.repositories.RoleRepository;
import com.israelgda.dscatalog.repositories.UserRepository;
import com.israelgda.dscatalog.services.exceptions.DataBaseException;
import com.israelgda.dscatalog.services.exceptions.ResourceNotFoundException;

@Service
public class UserService {

	private final BCryptPasswordEncoder passwordEncoder;
	
	private final UserRepository repository;
	
	private final RoleRepository roleRepository;
	

	public UserService(UserRepository repository, CategoryRepository categoryRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
		this.repository = repository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Transactional(readOnly = true)
	public UserDTO findById(Long id) {
		Optional<User> user = repository.findById(id);
		User result = user.orElseThrow(()-> new ResourceNotFoundException("Entity not found. Id: " + id));
		return new UserDTO(result);
	}

	@Transactional(readOnly = true)
	public Page<UserDTO> findAllPaged(Pageable pageable) {
		Page<User> listDTO = repository.findAll(pageable);
		return listDTO.map(x-> new UserDTO(x));
	}

	@Transactional
	public UserDTO create(UserInsertDTO userDTO) {
		User user = dtoToEntity(userDTO);
		user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
		user = repository.save(user);
		UserDTO result = new UserDTO(user);
		return result;
	}
	
	@Transactional
	public UserDTO update(Long id, UserDTO userDTO) {
		try {
			User newUser = repository.getOne(id);
			repository.save(updateUser(userDTO, newUser));
			return new UserDTO(newUser);
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
	private User dtoToEntity(UserDTO userDTO) {
		User user = new User();
		user.setFirstName(userDTO.getFirstName());
		user.setLastName(userDTO.getLastName());
		user.setEmail(userDTO.getEmail());
		
		user.getRoles().clear();
		for(RoleDTO roleDTO: userDTO.getRoles()) {
			Role role = roleRepository.getOne(roleDTO.getId());
			user.getRoles().add(role);
		}
		
		return user;
	}
	
	private User updateUser(UserDTO userDTO, User newUser) {
		newUser.setFirstName(userDTO.getFirstName());
		newUser.setLastName(userDTO.getLastName());
		newUser.setEmail(userDTO.getEmail());

		
		newUser.getRoles().clear();
		for(RoleDTO roleDTO: userDTO.getRoles()) {
			Role role = roleRepository.getOne(roleDTO.getId());
			newUser.getRoles().add(role);
		}
		
		return newUser;
	}

}
