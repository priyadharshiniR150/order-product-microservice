package com.example.order_service.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.common_module.Enum.OrderStatus;
import com.example.common_module.Enum.PaymentStatus;
import com.example.order_service.DTO.OrderResponseDto;
import com.example.order_service.DTO.PlaceOrderRequest;
import com.example.order_service.Entity.Address;
import com.example.order_service.Entity.Cart;
import com.example.order_service.Entity.Order;
import com.example.order_service.Entity.OrderItem;
import com.example.order_service.Repository.AddressRepository;
import com.example.order_service.Repository.CartRepository;
import com.example.order_service.Repository.OrderItemRepository;
import com.example.order_service.Repository.OrderRepository;
@Service
public class CartOrderService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private AddressRepository addressRepository;
    
    
    
    public OrderResponseDto placeOrder(PlaceOrderRequest request, String token) {

        Long userId = 1L;

        Address address = addressRepository.findByUserIdAndIsDefaultTrue(userId);

        if (address == null) {
            throw new RuntimeException("Default address not found");
        }

        List<Cart> carts = cartRepository.findByUserId(userId);

        if (carts.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Order order = new Order();

        order.setUserId(userId);
        order.setAddressId(address.getId());
        order.setPaymentMethod(request.getPaymentMethod());
        order.setPaymentStatus(PaymentStatus.PENDING);
        order.setStatus(OrderStatus.PENDING);

        double total = 0;

        for (Cart cart : carts) {
            total += cart.getTotalPrice();
        }

        order.setTotalAmount(total);

        order = orderRepository.save(order);
        
        for (Cart cart : carts) {

            OrderItem item = new OrderItem();

            item.setOrderId(order.getId());
            item.setProductId(cart.getProductId());
            item.setProductName(cart.getProductName());
            item.setQuantity(cart.getQuantity());
            item.setPrice(cart.getProductPrice());

            orderItemRepository.save(item);
        }
        cartRepository.deleteAll(carts);
        
        OrderResponseDto response = new OrderResponseDto();

        response.setOrderId(order.getId());
        response.setTotalAmount(order.getTotalAmount());
        response.setPaymentMethod(order.getPaymentMethod());
        response.setStatus(order.getStatus());
        response.setMessage("Order Placed Successfully");

        return response;

       
    }
}