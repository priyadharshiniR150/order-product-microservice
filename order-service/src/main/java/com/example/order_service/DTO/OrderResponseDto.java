package com.example.order_service.DTO;

import com.example.common_module.Enum.OrderStatus;

public class OrderResponseDto {
	private Long orderId;
	private Long productId;
	
	private int quantity;
	private double totalPrice;
	private String message;
	private OrderStatus status;
	
	//product details
	
		private String productName;
		private double productPrice;
		
		public OrderResponseDto() {
			
		}
	  
	public OrderResponseDto(Long orderId, Long productId, int quantity, double totalPrice, String productName,
			double productPrice) {
		super();
		this.orderId = orderId;
		this.productId = productId;
		this.quantity = quantity;
		this.totalPrice = totalPrice;
		this.productName = productName;
		this.productPrice = productPrice;
	}
	public OrderStatus getStatus() {
	    return status;
	}

	public void setStatus(OrderStatus status) {
	    this.status = status;
	}
	
	
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public double getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(double productPrice) {
		this.productPrice = productPrice;
	}
	public String getMessage() {
	    return message;
	}

	public void setMessage(String message) {
	    this.message = message;
	}
	
	
}
