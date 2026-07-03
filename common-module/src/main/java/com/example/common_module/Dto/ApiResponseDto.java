package com.example.common_module.Dto;

public class ApiResponseDto<T> {

    private String message;
    private T data;

    public ApiResponseDto() {
    }

    public ApiResponseDto(String message, T data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}