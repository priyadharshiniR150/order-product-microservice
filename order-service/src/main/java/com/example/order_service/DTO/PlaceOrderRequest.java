package com.example.order_service.DTO;


public class PlaceOrderRequest {

    private String paymentMethod;


    public String getPaymentMethod() {
        return paymentMethod;
    }


    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}