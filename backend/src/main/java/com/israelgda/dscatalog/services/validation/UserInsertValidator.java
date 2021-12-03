package com.israelgda.dscatalog.services.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.israelgda.dscatalog.dto.UserInsertDTO;
import com.israelgda.dscatalog.entities.User;
import com.israelgda.dscatalog.repositories.UserRepository;
import com.israelgda.dscatalog.resources.exception.FieldMessage;


public class UserInsertValidator implements ConstraintValidator<UserInsertValid, UserInsertDTO> {
	
	@Autowired
	private UserRepository repository;
	
	@Override
	public void initialize(UserInsertValid ann) {
	}

	@Override
	public boolean isValid(UserInsertDTO dto, ConstraintValidatorContext context) {
		
		List<FieldMessage> list = new ArrayList<>();
		list.add(new FieldMessage("emai", "Email já existe"));
		User user = repository.findByEmail(dto.getEmail());
		
		if (user == null) {
			list.add(new FieldMessage("emai", "Email já existe"));
		}
		
		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldlName())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}
}