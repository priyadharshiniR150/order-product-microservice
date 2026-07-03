package com.example.common_module.Exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.common_module.Constants.AppConstants;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ProductNotFoundException.class)
	public ResponseEntity<Map<String, String>> productException(ProductNotFoundException ex) {

	    Map<String, String> response = new HashMap<>();
	    response.put("status", AppConstants.FAILED);
	    response.put("message", ex.getMessage());

	    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}


	@ExceptionHandler(OrderNotFoundException.class)
	public ResponseEntity<Map<String, String>> orderException(OrderNotFoundException ex) {

	    Map<String, String> response = new HashMap<>();
	    response.put("status", AppConstants.FAILED);
	    response.put("message", ex.getMessage());

	    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> exception(Exception ex) {

        Map<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}