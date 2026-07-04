package com.example.product_service.Controller;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.common_module.Constants.AppConstants;
import com.example.common_module.Dto.ProductDto;
import com.example.common_module.Exception.ProductNotFoundException;
import com.example.product_service.Entity.Product;
import com.example.product_service.Repository.ProductRepository;

@RestController
@RequestMapping("/products")
public class ProductController {
	@Autowired
	private ProductRepository productRepo;
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	//create a product
	
	
	@PostMapping
	public ResponseEntity<Map<String, Object>> addProduct(@RequestBody Product product) {

	    Product savedProduct = productRepo.save(product);

	    ProductDto dto = new ProductDto();
	    dto.setId(savedProduct.getId());
	    dto.setName(savedProduct.getName());
	    dto.setPrice(savedProduct.getPrice());

	    redisTemplate.delete("products:all");
	    redisTemplate.opsForValue().set("product:" + savedProduct.getId(), dto);

	    Map<String, Object> response = new HashMap<>();
	    response.put("message", AppConstants.SUCCESS);
	    response.put("data", savedProduct);

	    return ResponseEntity.ok(response);
	}

	@SuppressWarnings("unchecked")
	@GetMapping
	public List<ProductDto> getAllProduct() {

	    String key = "products:all";

	    List<ProductDto> cached =
	        (List<ProductDto>) redisTemplate.opsForValue().get(key);

	    if (cached != null) {
	        System.out.println("Products fetched from Redis");
	        return cached;
	    }

	    System.out.println("Products fetched from DB");

	    List<ProductDto> products = productRepo.findAll()
	        .stream()
	        .map(p -> {
	            ProductDto dto = new ProductDto();
	            dto.setId(p.getId());
	            dto.setName(p.getName());
	            dto.setPrice(p.getPrice());
	            return dto;
	        })
	        .toList();

	    redisTemplate.opsForValue().set(key, products);

	    return products;
	}
	@GetMapping("/{productId}")
	public ResponseEntity<ProductDto> getProductById(@PathVariable Long productId) {

	    String key = "product:" + productId;

	    ProductDto cached = (ProductDto) redisTemplate.opsForValue().get(key);

	    if (cached != null) {
	        System.out.println("Product fetched from Redis");
	        return ResponseEntity.ok(cached);
	    }

	    System.out.println("Product fetched from DB");

	    Product product = productRepo.findById(productId)
	            .orElseThrow(() -> new ProductNotFoundException(AppConstants.PRODUCT_NOT_FOUND));

	    ProductDto dto = new ProductDto();
	    dto.setId(product.getId());
	    dto.setName(product.getName());
	    dto.setPrice(product.getPrice());

	    redisTemplate.opsForValue().set(key, dto);

	    return ResponseEntity.ok(dto);
	}
	
	
	
	
	}