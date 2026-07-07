package com.example.order_service.Controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.common_module.Constants.AppConstants;
import com.example.common_module.Dto.ProductDto;
import com.example.common_module.Enum.OrderStatus;
import com.example.common_module.Exception.OrderNotFoundException;
import com.example.order_service.DTO.OrderResponseDto;
import com.example.order_service.Entity.Order;
import com.example.order_service.Repository.OrderRepository;

@RestController

@RequestMapping("/orders")
public class OrderController {
	
	@Autowired
	private OrderRepository orderrepo;
	@Autowired
	private WebClient.Builder WebClientBuilder;
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	
	//create method to place order
	@PostMapping("/placeOrder")
	public ResponseEntity<OrderResponseDto> placeOrder(
	        @RequestBody Order order,
	        @RequestHeader("Authorization") String token) {

	    order.setStatus(OrderStatus.PENDING);
	    
	
	    System.out.println("TOKEN FROM GATEWAY = " + token);
	    ProductDto productDto = getProduct(order.getProductId(), token);

	    OrderResponseDto responseDto = new OrderResponseDto();
	    responseDto.setProductId(order.getProductId());
	    responseDto.setQuantity(order.getQuantity());
	    responseDto.setStatus(order.getStatus());

	    responseDto.setProductName(productDto.getName());
	    responseDto.setProductPrice(productDto.getPrice());
	    responseDto.setTotalPrice(order.getQuantity() * productDto.getPrice());

	    orderrepo.save(order);
	    redisTemplate.delete("orders:list");

	    responseDto.setOrderId(order.getId());
	    responseDto.setMessage(AppConstants.SUCCESS);

	    return ResponseEntity.ok(responseDto);  
	   
	}


	@SuppressWarnings("unchecked")
	@GetMapping("/list")
	public List<OrderResponseDto> getAllOrders(
	        @RequestHeader("Authorization") String token) {

	    String key = "orders:list";

	    List<OrderResponseDto> cached =
	        (List<OrderResponseDto>) redisTemplate.opsForValue().get(key);

	    if (cached != null) {
	        System.out.println("Orders fetched from Redis");
	        return cached;
	    }

	    System.out.println("Orders fetched from DB");

	    List<Order> orders = orderrepo.findAll();

	    List<OrderResponseDto> response = orders.stream()
	            .map(order -> {
	            	ProductDto product = getProduct(order.getProductId(), token);

	                OrderResponseDto dto = new OrderResponseDto();
	                dto.setOrderId(order.getId());
	                dto.setProductId(order.getProductId());
	                dto.setProductName(product.getName());
	                dto.setQuantity(order.getQuantity());
	                dto.setStatus(order.getStatus());
	                dto.setProductPrice(product.getPrice());
	                dto.setTotalPrice(order.getQuantity() * product.getPrice());

	                return dto;
	            }).toList();

	    redisTemplate.opsForValue().set(key, response);

	    return response;
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<OrderResponseDto> getOrderById(
	        @PathVariable Long id,
	        @RequestHeader("Authorization") String token) {

	    Order order = orderrepo.findById(id)
	            .orElseThrow(() -> new OrderNotFoundException(AppConstants.ORDER_NOT_FOUND));
	    ProductDto product = getProduct(order.getProductId(), token);

	    OrderResponseDto response = new OrderResponseDto();
	    response.setOrderId(order.getId());
	    response.setProductId(order.getProductId());
	    response.setProductName(product.getName());
	    response.setQuantity(order.getQuantity());
	    response.setStatus(order.getStatus());
	    response.setProductPrice(product.getPrice());
	    response.setTotalPrice(order.getQuantity() * product.getPrice());
	    response.setMessage(AppConstants.SUCCESS);

	    return ResponseEntity.ok(response);
	}
	@PutMapping("/confirm/{id}")
	public ResponseEntity<Order> confirmOrder(@PathVariable Long id) {

	    Order order = orderrepo.findById(id)
	            .orElseThrow(() -> new OrderNotFoundException(AppConstants.ORDER_NOT_FOUND));

	    order.setStatus(OrderStatus.CONFIRMED);
	    orderrepo.save(order);

	    return ResponseEntity.ok(order);
	} 
	
	@PutMapping("/cancel/{id}")
	public ResponseEntity<Order> cancelOrder(@PathVariable Long id) {

	    Order order = orderrepo.findById(id)
	            .orElseThrow(() -> new OrderNotFoundException(AppConstants.ORDER_NOT_FOUND));

	    order.setStatus(OrderStatus.CANCELLED);
	    orderrepo.save(order);

	    return ResponseEntity.ok(order);
	}
	private ProductDto getProduct(Long productId, String token) {

	    String key = "product:" + productId;

	    ProductDto product = (ProductDto) redisTemplate.opsForValue().get(key);

	    if (product != null) {
	        System.out.println("Data fetched from Redis");
	        return product;
	    }

	    System.out.println("Calling Product Service...");

	    product = WebClientBuilder.build()
	            .get()
	            .uri("http://localhost:8081/products/" + productId)
	            .header("Authorization", token)
	            .retrieve()
	            .bodyToMono(ProductDto.class)
	            .block();

	    redisTemplate.opsForValue().set(key, product);

	    return product;
	}}