package com.israelgda.dscatalog.resources.exception;

public class FieldMessage {

	private String fieldlName;
	private String message;
	
	public FieldMessage() {
		
	}

	public FieldMessage(String fieldlName, String message) {
		super();
		this.fieldlName = fieldlName;
		this.message = message;
	}

	public String getFieldlName() {
		return fieldlName;
	}

	public void setFieldlName(String fieldlName) {
		this.fieldlName = fieldlName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
