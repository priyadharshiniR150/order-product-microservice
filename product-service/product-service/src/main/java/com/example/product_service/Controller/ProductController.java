package com.example.product_service.Controller;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.common_module.Constants.AppConstants;
import com.example.common_module.Exception.ProductNotFoundException;
import com.example.product_service.Entity.Product;
import com.example.product_service.Repository.ProductRepository;

@RestController
@RequestMapping("/products")
public class ProductController {
	@Autowired
	private ProductRepository productRepo;

	
	//create a product
	
	
	@PostMapping
	public ResponseEntity<Map<String, Object>> addProduct(@RequestBody Product product) {

	    Product savedProduct = productRepo.save(product);

	    Map<String, Object> response = new HashMap<>();
	    response.put("message", AppConstants.SUCCESS);
	    response.put("data", savedProduct);

	    return ResponseEntity.ok(response);
	}
	

	//GetAll Products
	@GetMapping
	public List<Product>getAllProduct(){
		return productRepo.findAll();
	}
	
	//GetProduct ById
	@GetMapping("/{productId}")
	public ResponseEntity<Product> getProductById(@PathVariable Long productId) {

	    Product product = productRepo.findById(productId)
	            .orElseThrow(() -> new ProductNotFoundException(AppConstants.PRODUCT_NOT_FOUND));

	    return ResponseEntity.ok(product);
	}
	
	
	
	
	
	}
