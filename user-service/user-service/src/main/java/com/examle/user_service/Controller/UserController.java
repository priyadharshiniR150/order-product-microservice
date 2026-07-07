package com.examle.user_service.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.examle.user_service.DTO.AuthResponsedto;
import com.examle.user_service.DTO.LoginRequestdto;
import com.examle.user_service.DTO.RegisterRequestdto;
import com.examle.user_service.Service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequestdto request) {
        return ResponseEntity.ok(userService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponsedto> login(@RequestBody LoginRequestdto request) {
        return ResponseEntity.ok(userService.login(request));
    
    }
}