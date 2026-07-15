package com.example.order_service.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.common_module.Constants.AppConstants;
import com.example.common_module.Dto.ProductDto;
import com.example.common_module.Enum.OrderStatus;
import com.example.common_module.Enum.PaymentStatus;
import com.example.common_module.Exception.OrderNotFoundException;
import com.example.order_service.DTO.BuyNowRequestDto;
import com.example.order_service.DTO.OrderResponseDto;
import com.example.order_service.Entity.Address;
import com.example.order_service.Entity.Order;
import com.example.order_service.Entity.OrderItem;
import com.example.order_service.Repository.AddressRepository;
import com.example.order_service.Repository.OrderItemRepository;
import com.example.order_service.Repository.OrderRepository;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderrepo;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private AddressRepository addressrepo;

  
    public OrderResponseDto buyNow(BuyNowRequestDto request, String token) {

        ProductDto product = getProduct(request.getProductId(), token);

        Order order = new Order();
        order.setUserId(1L);
        order.setAddressId(1L);
        order.setPaymentMethod(request.getPaymentMethod());
        order.setPaymentStatus(PaymentStatus.PENDING);
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmount(product.getPrice() * request.getQuantity());

        order = orderrepo.save(order);

        OrderItem item = new OrderItem();
        item.setOrderId(order.getId());
        item.setProductId(product.getId());
        item.setProductName(product.getName());
        item.setQuantity(request.getQuantity());
        item.setPrice(product.getPrice());

        orderItemRepository.save(item);

        OrderResponseDto response = new OrderResponseDto();
        response.setOrderId(order.getId());
        response.setTotalAmount(order.getTotalAmount());
        response.setPaymentMethod(order.getPaymentMethod());
        response.setStatus(order.getStatus());
        response.setMessage("Buy Now Order Placed Successfully");

        return response;
    }

    public OrderResponseDto getOrderById(Long id, String token) {

        Order order = orderrepo.findById(id)
                .orElseThrow(() ->
                        new OrderNotFoundException(AppConstants.ORDER_NOT_FOUND));

        OrderResponseDto response = new OrderResponseDto();

        response.setOrderId(order.getId());
        response.setTotalAmount(order.getTotalAmount());
        response.setPaymentMethod(order.getPaymentMethod());
        response.setStatus(order.getStatus());
        response.setMessage(AppConstants.SUCCESS);

        return response;
    }
    public List<OrderResponseDto> getAllOrders(String token) {

        List<Order> orders = orderrepo.findAll();

        return orders.stream().map(order -> {

            OrderResponseDto dto = new OrderResponseDto();

            dto.setOrderId(order.getId());
            dto.setTotalAmount(order.getTotalAmount());
            dto.setPaymentMethod(order.getPaymentMethod());
            dto.setStatus(order.getStatus());

            Address address =
                    addressrepo.findById(order.getAddressId()).orElse(null);

            if (address != null) {

                dto.setFullName(address.getName());
                dto.setMobileNumber(address.getPhone());
                dto.setStreet(address.getStreet());
                dto.setCity(address.getCity());
                dto.setState(address.getState());
                dto.setPincode(address.getPincode());

            }

            List<OrderItem> items =
                    orderItemRepository.findByOrderId(order.getId());

            if (!items.isEmpty()) {

                OrderItem item = items.get(0);

                dto.setProductName(item.getProductName());
                dto.setQuantity(item.getQuantity());
                dto.setPrice(item.getPrice());

                ProductDto product =
                        getProduct(item.getProductId(), token);

                if (product != null) {
                    dto.setImageUrl(product.getImageUrl());
                }

            }

            return dto;

        }).toList();

    }
    public Order confirmOrder(Long id) {
    	  Order order = orderrepo.findById(id)
  	            .orElseThrow(() -> new OrderNotFoundException(AppConstants.ORDER_NOT_FOUND));

  	    order.setStatus(OrderStatus.CONFIRMED);
  	    orderrepo.save(order);

  	    return order;
    }
    public Order packOrder(Long id) {

        Order order = orderrepo.findById(id)
                .orElseThrow(() ->
                        new OrderNotFoundException(AppConstants.ORDER_NOT_FOUND));

        order.setStatus(OrderStatus.PACKED);

        orderrepo.save(order);

        return order;
    }
    
    public Order shipOrder(Long id) {

        Order order = orderrepo.findById(id)
                .orElseThrow(() ->
                        new OrderNotFoundException(AppConstants.ORDER_NOT_FOUND));

        order.setStatus(OrderStatus.SHIPPED);

        orderrepo.save(order);

        return order;
    }
    public Order outForDelivery(Long id) {

        Order order = orderrepo.findById(id)
                .orElseThrow(() ->
                        new OrderNotFoundException(AppConstants.ORDER_NOT_FOUND));

        order.setStatus(OrderStatus.OUT_FOR_DELIVERY);

        orderrepo.save(order);

        return order;
    }
    public Order cancelOrder(Long id) {
    	 Order order = orderrepo.findById(id)
 	            .orElseThrow(() -> new OrderNotFoundException(AppConstants.ORDER_NOT_FOUND));

 	    order.setStatus(OrderStatus.CANCELLED);
 	    orderrepo.save(order);

 	    return order;
    }
    public Order deliverOrder(Long id) {

        Order order = orderrepo.findById(id)
                .orElseThrow(() ->
                        new OrderNotFoundException(AppConstants.ORDER_NOT_FOUND));

        order.setStatus(OrderStatus.DELIVERED);
        order.setPaymentStatus(PaymentStatus.COMPLETED);

        orderrepo.save(order);

        return order;
    }
    public void deleteOrder(Long id) {

        Order order = orderrepo.findById(id)
                .orElseThrow(() ->
                        new OrderNotFoundException(AppConstants.ORDER_NOT_FOUND));

        orderrepo.delete(order);
    }
    public void deleteAllOrders() {

        orderItemRepository.deleteAll();
        orderrepo.deleteAll();
    }
    private ProductDto getProduct(Long productId, String token) {
    	 String key = "product:" + productId;

 	    ProductDto product = (ProductDto) redisTemplate.opsForValue().get(key);

 	    if (product != null) {
 	        System.out.println("Data fetched from Redis");
 	        return product;
 	    }

 	    System.out.println("Calling Product Service...");

 	   product = webClientBuilder.build()
 	            .get()
 	            .uri("http://localhost:8081/products/" + productId)
 	            .header("Authorization", token)
 	            .retrieve()
 	            .bodyToMono(ProductDto.class)
 	            .block();

 	    redisTemplate.opsForValue().set(key, product);

 	    return product;
    }
  
}
