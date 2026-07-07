package com.examle.user_service.DTO;

public class AuthResponsedto {

    private String token;

    public AuthResponsedto() {
    }

    public AuthResponsedto(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}