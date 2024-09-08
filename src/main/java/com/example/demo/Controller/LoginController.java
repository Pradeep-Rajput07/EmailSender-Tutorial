package com.example.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.Dto.LoginDTO;
import com.example.demo.Service.LoginService;

@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;
    

    @PostMapping("/api/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO) {
        
        if (loginDTO.getEmail() == null || loginDTO.getPassword() == null) {
            return ResponseEntity.badRequest().body("Email and password are required.");
        }

      
        String result = loginService.userVerify(loginDTO);
        switch (result) {
            case "Login successful":
                return ResponseEntity.ok(result);
            case "Incorrect password":
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
            case "Email not found":
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
            default:
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }
}
