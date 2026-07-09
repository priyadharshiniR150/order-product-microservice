package com.example.order_service.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.common_module.Constants.AppConstants;
import com.example.common_module.Dto.ProductDto;
import com.example.order_service.DTO.AddCartRequestDto;
import com.example.order_service.DTO.CartResponseDto;
import com.example.order_service.Entity.Cart;
import com.example.order_service.Repository.CartRepository;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public CartResponseDto addToCart(AddCartRequestDto request, String token) {

        ProductDto product = getProduct(request.getProductId(), token);

        Cart cart = new Cart();

        
        cart.setUserId(1L);

        cart.setProductId(product.getId());
        cart.setProductName(product.getName());
        cart.setProductPrice(product.getPrice());
        cart.setQuantity(request.getQuantity());
        cart.setTotalPrice(product.getPrice() * request.getQuantity());

        cartRepository.save(cart);

        CartResponseDto response = new CartResponseDto();
      
        response.setProductId(cart.getProductId());
        response.setProductName(cart.getProductName());
        response.setImageUrl(product.getImageUrl());
        response.setProductPrice(cart.getProductPrice());
        response.setQuantity(cart.getQuantity());
        response.setTotalPrice(cart.getTotalPrice());
        response.setMessage(AppConstants.SUCCESS);

        return response;
    }

    private ProductDto getProduct(Long productId, String token) {

        String key = "product:" + productId;

        ProductDto product = (ProductDto) redisTemplate.opsForValue().get(key);

        if (product != null) {
            System.out.println("Product fetched from Redis");
            return product;
        }

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

   
    	public List<CartResponseDto> getAllCart(String token) {

    	    Long userId = 1L;

    	    List<Cart> carts = cartRepository.findByUserId(userId);

    	    return carts.stream().map(cart -> {

    	        ProductDto product = getProduct(cart.getProductId(), token);

    	        CartResponseDto dto = new CartResponseDto();

    	        dto.setProductId(cart.getProductId());
    	        dto.setProductName(cart.getProductName());
    	        dto.setProductPrice(cart.getProductPrice());
    	        dto.setQuantity(cart.getQuantity());
    	        dto.setTotalPrice(cart.getTotalPrice());
    	        dto.setImageUrl(product.getImageUrl());

    	        return dto;

    	    }).toList();
    	}
    
    
    
    public Cart updateCart(Long id, Cart cartRequest) {

        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cart Item Not Found"));

        cart.setQuantity(cartRequest.getQuantity());

        cart.setTotalPrice(
                cart.getProductPrice() * cart.getQuantity());

        return cartRepository.save(cart);
    }
    
    public String removeCart(Long id) {

        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cart Item Not Found"));

        cartRepository.delete(cart);

        return "Product removed from cart successfully";
    }
    
    
    public String clearCart() {

        Long userId = 1L;

        List<Cart> carts = cartRepository.findByUserId(userId);

        cartRepository.deleteAll(carts);

        return "Cart cleared successfully";
    }
}