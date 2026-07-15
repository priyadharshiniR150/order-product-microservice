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

import com.example.order_service.DTO.BuyNowRequestDto;
import com.example.order_service.DTO.OrderResponseDto;
import com.example.order_service.DTO.PlaceOrderRequest;
import com.example.order_service.Entity.Order;
import com.example.order_service.Service.CartOrderService;
import com.example.order_service.Service.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController1{

    @Autowired
    private OrderService orderService;
    @Autowired
    private CartOrderService cartOrderService;
    

    @PostMapping("/buyNow")
    public ResponseEntity<OrderResponseDto> buyNow(
            @RequestBody BuyNowRequestDto request,
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(orderService.buyNow(request, token));
    }
    @GetMapping("/list")
    public List<OrderResponseDto> getAllOrders(
            @RequestHeader("Authorization") String token) {

        return orderService.getAllOrders(token);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> getOrderById(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(orderService.getOrderById(id, token));
    }

    @PutMapping("/confirm/{id}")
    public ResponseEntity<Order> confirmOrder(@PathVariable Long id) {

        return ResponseEntity.ok(orderService.confirmOrder(id));
    }

    @PutMapping("/cancel/{id}")
    public ResponseEntity<Order> cancelOrder(@PathVariable Long id) {

        return ResponseEntity.ok(orderService.cancelOrder(id));
    }
    @PostMapping("/placeOrder")
    public OrderResponseDto placeOrder(
            @RequestBody PlaceOrderRequest request,
            @RequestHeader("Authorization") String token) {

        return cartOrderService.placeOrder(request, token);
    }
    @PutMapping("/pack/{id}")
    public ResponseEntity<Order> packOrder(@PathVariable Long id) {

        return ResponseEntity.ok(orderService.packOrder(id));
    }
    @PutMapping("/ship/{id}")
    public ResponseEntity<Order> shipOrder(@PathVariable Long id) {

        return ResponseEntity.ok(orderService.shipOrder(id));
    }
    @PutMapping("/out-for-delivery/{id}")
    public ResponseEntity<Order> outForDelivery(@PathVariable Long id) {

        return ResponseEntity.ok(orderService.outForDelivery(id));
    }
    @PutMapping("/deliver/{id}")
    public ResponseEntity<Order> deliverOrder(@PathVariable Long id) {

        return ResponseEntity.ok(orderService.deliverOrder(id));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.ok("Order deleted successfully");
    }
    @DeleteMapping("/deleteAll")
    public ResponseEntity<String> deleteAllOrders() {

        orderService.deleteAllOrders();
        return ResponseEntity.ok("All orders deleted successfully");
    }
}