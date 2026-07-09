package com.example.order_service.DTO;

import com.example.common_module.Enum.OrderStatus;
import com.example.common_module.Enum.PaymentStatus;

public class OrderResponseDto {
	private Long orderId;
	private Double totalAmount;

	private String paymentMethod;

	private OrderStatus status;

	private String message;
	
	public   OrderResponseDto(){
		
	} 

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


}