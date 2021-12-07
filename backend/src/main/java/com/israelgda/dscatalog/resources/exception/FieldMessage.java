package com.israelgda.dscatalog.resources.exception;

public class FieldMessage {

	private String fieldName;
	private String message;
	
	public FieldMessage() {
		
	}

	public FieldMessage(String fieldName, String message) {
		super();
		this.fieldName = fieldName;
		this.message = message;
	}

	public String getFieldlName() {
		return fieldName;
	}

	public void setFieldlName(String fieldlName) {
		this.fieldName = fieldlName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
