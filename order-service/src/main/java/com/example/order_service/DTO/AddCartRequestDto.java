package com.example.order_service.DTO;

public class AddCartRequestDto {


	

	    private Long productId;
	    private Integer quantity;

	    public AddCartRequestDto() {
	    }

	    public Long getProductId() {
	        return productId;
	    }

	    public void setProductId(Long productId) {
	        this.productId = productId;
	    }

	    public Integer getQuantity() {
	        return quantity;
	    }

	    public void setQuantity(Integer quantity) {
	        this.quantity = quantity;
	    }
	}

