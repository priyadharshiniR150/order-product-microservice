package com.examle.user_service.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.examle.user_service.DTO.AuthResponsedto;
import com.examle.user_service.DTO.LoginRequestdto;
import com.examle.user_service.DTO.RegisterRequestdto;
import com.examle.user_service.Repository.UserRepo;
import com.examle.user_service.entity.UserEntity;


@Service
public class UserService {

	
	 @Autowired
	    private UserRepo userRepository;

	    @Autowired
	    private PasswordEncoder passwordEncoder;
	    @Autowired
	
	    private JwtService jwtService;

	    public String register(RegisterRequestdto request) {

	        // Check email already exists
	    	if (!request.getRole().equals("ADMIN") &&
	    		    !request.getRole().equals("CUSTOMER")) {

	    		    throw new RuntimeException("Invalid Role");
	    		}
	        // Create User Entity
	        UserEntity user = new UserEntity();
	        user.setUsername(request.getUsername());
	        user.setEmail(request.getEmail());
	        user.setPassword(passwordEncoder.encode(request.getPassword()));
	        user.setRole(request.getRole());

	        // Save to Database
	        userRepository.save(user);

	        return "User Registered Successfully";
	    }
	    
	    public AuthResponsedto login(LoginRequestdto request) {

	        UserEntity user = userRepository.findByEmail(request.getEmail())
	                .orElseThrow(() -> new RuntimeException("User Not Found"));

	        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
	            throw new RuntimeException("Invalid Password");
	        }
	        String token = jwtService.generateToken(
	                user.getEmail(),
	                user.getRole());

	        return new AuthResponsedto(token);
	    }
}
