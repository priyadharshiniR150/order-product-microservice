package com.example.order_service.Controller;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.order_service.DTO.AddCartRequestDto;
import com.example.order_service.DTO.CartResponseDto;
import com.example.order_service.Entity.Cart;
import com.example.order_service.Service.CartService;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<CartResponseDto> addToCart(
            @RequestBody AddCartRequestDto request,
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(cartService.addToCart(request, token));
    }
    
    @GetMapping("/list")
    public ResponseEntity<List<CartResponseDto>> getAllCart(
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(cartService.getAllCart(token));
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<Cart> updateCart(
            @PathVariable Long id,
            @RequestBody Cart cart) {

        return ResponseEntity.ok(cartService.updateCart(id, cart));
    }
    
    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> removeCart(
            @PathVariable Long id) {

        return ResponseEntity.ok(cartService.removeCart(id));
    }
    
    
    @DeleteMapping("/clear")
    public ResponseEntity<String> clearCart() {

        return ResponseEntity.ok(cartService.clearCart());
    }
}