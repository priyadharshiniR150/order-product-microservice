package com.example.order_service.Controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

import reactor.core.publisher.Mono;

@RestController

@RequestMapping("/orders")
public class OrderController {
	
	@Autowired
	private OrderRepository orderrepo;
	@Autowired
	private WebClient.Builder WebClientBuilder;
	
	//create method to place order
	@PostMapping("/placeOrder")
	public Mono<ResponseEntity<OrderResponseDto>> placeOrder(@RequestBody Order order){
		  order.setStatus(OrderStatus.PENDING);
		
		//fetch product details from product service
		
		return WebClientBuilder.build().get().uri("http://localhost:8081/products/" + order.getProductId()).retrieve()	
				.bodyToMono(ProductDto.class).map(productDto->{ 
					OrderResponseDto responseDto= new OrderResponseDto();
					responseDto.setProductId(order.getProductId());
					responseDto.setQuantity(order.getQuantity());
					responseDto.setStatus(order.getStatus()); 
					//set productDetails
					
					responseDto.setProductName(productDto.getName());
					responseDto.setProductPrice(productDto.getPrice());
					responseDto.setTotalPrice(order.getQuantity() * productDto.getPrice());
					
					//save order details to DB
					
					orderrepo.save(order);
					responseDto.setOrderId(order.getId());
					responseDto.setMessage(AppConstants.SUCCESS);
					return ResponseEntity.ok(responseDto);


					
				});
		
	}


	@GetMapping("/list")
	public List<OrderResponseDto> getAllOrders() {
		

	    List<Order> orders = orderrepo.findAll();

	    return orders.stream().map(order -> {

	        ProductDto product = WebClientBuilder.build()
	                .get()
	                .uri("http://localhost:8081/products/" + order.getProductId())
	                .retrieve()
	                .bodyToMono(ProductDto.class)
	                .block();

	        OrderResponseDto dto = new OrderResponseDto();
	        dto.setOrderId(order.getId());
	        dto.setProductId(order.getProductId());
	        dto.setProductName(product.getName());
	        dto.setQuantity(order.getQuantity());
	        dto.setStatus(order.getStatus());
	        dto.setProductPrice(product.getPrice());
	        dto.setTotalPrice(order.getQuantity() * product.getPrice());
	        dto.setMessage(AppConstants.SUCCESS);

	        return dto;

	    }).toList();
	}
	
	@GetMapping("/{id}")
	public Mono<ResponseEntity<OrderResponseDto>> getOrderById(@PathVariable Long id) {

	    Order order = orderrepo.findById(id)
	            .orElseThrow(() -> new OrderNotFoundException(AppConstants.ORDER_NOT_FOUND));

	    return WebClientBuilder.build()
	            .get()
	            .uri("http://localhost:8081/products/" + order.getProductId())
	            .retrieve()
	            .bodyToMono(ProductDto.class)
	            .map(product -> {

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
	            });}
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
	
}
