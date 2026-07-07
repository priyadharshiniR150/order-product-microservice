package com.examle.user_service.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginRequestdto {
	   public LoginRequestdto(@Email String email, @NotBlank String password) {
		super();
		this.email = email;
		this.password = password;
	}
	   public  LoginRequestdto() {
		   
	   }


	   public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	   @Email
	    private String email;

	    @NotBlank
	    private String password;
}
