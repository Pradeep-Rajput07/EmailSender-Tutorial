package com.example.demo.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Dto.RegisterDTO;

import com.example.demo.Entity.RegisterEntity;
import com.example.demo.Repository.LoginRepo;
import com.example.demo.Repository.RegisterRepository;

@Service

public class RegisterService {
	@Autowired
	RegisterRepository registerRepository;

	@Autowired
	LoginRepo loginRepo;



	 public String saveDetails(RegisterDTO registerDTO) {
	        if (registerDTO != null) {
	            // Validate that passwords match
	            if (!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())) {
	                return "Passwords do not match";
	            }

	            RegisterEntity registerEntity = new RegisterEntity();
	            registerEntity.setEmail(registerDTO.getEmail());
	            registerEntity.setPassword(registerDTO.getPassword());

	            // Check if the email is already in use
	            Optional<RegisterEntity> existingUser = registerRepository.findByEmail(registerDTO.getEmail());
	            if (existingUser.isPresent()) {
	                return "Email is already in use";
	            }

	            // Save the new user
	            registerRepository.save(registerEntity);
	            return "Successfully Registered";
	        } else {
	            return "Registration failed";
	        }
	    }
    


	 public String validateCredentials(String email, String password) {
		    if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
		        return "Data is not sufficient!!";
		    }

		    // Trim and normalize the email to avoid whitespace issues and ensure case-insensitivity
		    String normalizedEmail = email.trim().toLowerCase();

		    Optional<RegisterEntity> registerEntityOpt = registerRepository.findByEmail(normalizedEmail);
		    if (registerEntityOpt.isPresent()) {
		        RegisterEntity registerEntity = registerEntityOpt.get();
		        if (registerEntity.getPassword().equals(password)) {
		            return "home";
		        } else {
		            return "invalid-password";
		        }
		    } else {
		        return "email-not-found";
		    }
		}

}
