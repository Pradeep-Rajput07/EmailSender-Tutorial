package com.example.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.Dto.RegisterDTO;
import com.example.demo.Service.RegisterService;

@RestController
@RequestMapping("/api")
public class RegistrationController {

    @Autowired
    private RegisterService registerService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDTO registerDTO) {
        String result = registerService.saveDetails(registerDTO);
        if ("Successfully Registered".equals(result)) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }
}
