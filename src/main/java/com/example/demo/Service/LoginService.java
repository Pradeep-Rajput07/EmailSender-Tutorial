package com.example.demo.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.Dto.LoginDTO;
import com.example.demo.Entity.RegisterEntity;
import com.example.demo.Repository.LoginRepo;

@Service
public class LoginService {

    @Autowired
    private LoginRepo loginRepo;

    public String userVerify(LoginDTO loginDTO) {
        if (loginDTO.getEmail() == null || loginDTO.getPassword() == null) {
            return "Invalid details";
        }

        RegisterEntity user = loginRepo.findByEmail(loginDTO.getEmail()).orElse(null);

        if (user != null) {
            if (user.getPassword().equals(loginDTO.getPassword())) {
                return "Login successful";
            } else {
                return "Incorrect password";
            }
        } else {
            return "Email not found";
        }
    }
}
