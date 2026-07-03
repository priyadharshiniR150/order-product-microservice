package com.example.common_module.Exception;




public class OrderNotFoundException extends RuntimeException {

	
	private static final long serialVersionUID = 2642175827832204444L;

	public OrderNotFoundException(String message) {
        super(message);
    }}