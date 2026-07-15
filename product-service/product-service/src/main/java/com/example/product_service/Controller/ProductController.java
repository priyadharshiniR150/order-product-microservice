package com.example.product_service.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.common_module.Constants.AppConstants;
import com.example.common_module.Dto.ProductDto;
import com.example.common_module.Exception.ProductNotFoundException;
import com.example.common_module.Util.UtilRedis;
import com.example.product_service.Entity.Product;
import com.example.product_service.Repository.ProductRepository;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private UtilRedis utilRedis;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // Add Product
    @PostMapping
    public ResponseEntity<Map<String, Object>> addProduct(@RequestBody Product product) {

        Product savedProduct = productRepo.save(product);

        ProductDto dto = new ProductDto();
        dto.setId(savedProduct.getId());
        dto.setName(savedProduct.getName());
        dto.setPrice(savedProduct.getPrice());
        dto.setImageUrl(savedProduct.getImageUrl());

        // Clear product list cache
        redisTemplate.delete("products:all");

        // Save single product cache
        redisTemplate.opsForValue().set("product:" + savedProduct.getId(), dto);

        Map<String, Object> response = new HashMap<>();
        response.put("message", AppConstants.SUCCESS);
        response.put("data", savedProduct);

        return ResponseEntity.ok(response);
    }

    // Get All Products
    @GetMapping
    @SuppressWarnings("unchecked")
    public List<ProductDto> getAllProduct() {

        return utilRedis.getOrSet(
                "products:all",
                List.class,
                () -> productRepo.findAll()
                        .stream()
                        .map(product -> {
                            ProductDto dto = new ProductDto();
                            dto.setId(product.getId());
                            dto.setName(product.getName());
                            dto.setPrice(product.getPrice());
                            dto.setImageUrl(product.getImageUrl());
                            return dto;
                        })
                        .toList()
        );
    }

    // Get Product By Id
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long productId) {

        ProductDto dto = utilRedis.getOrSet(
                "product:" + productId,
                ProductDto.class,
                () -> {

                    Product product = productRepo.findById(productId)
                            .orElseThrow(() ->
                                    new ProductNotFoundException(AppConstants.PRODUCT_NOT_FOUND));

                    ProductDto p = new ProductDto();
                    p.setId(product.getId());
                    p.setName(product.getName());
                    p.setPrice(product.getPrice());
                    p.setImageUrl(product.getImageUrl());

                    return p;
                });

        return ResponseEntity.ok(dto);
    }

    // Delete Product
    @DeleteMapping("/{productId}")
    public ResponseEntity<Map<String, Object>> deleteProduct(@PathVariable Long productId) {

        Product product = productRepo.findById(productId)
                .orElseThrow(() ->
                        new ProductNotFoundException(AppConstants.PRODUCT_NOT_FOUND));

        productRepo.delete(product);

        // Remove cache
        redisTemplate.delete("products:all");
        redisTemplate.delete("product:" + productId);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Product deleted successfully");

        return ResponseEntity.ok(response);
    }

}