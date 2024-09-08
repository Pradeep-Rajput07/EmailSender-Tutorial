package com.example.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.Service.RegisterService;


@Controller

public class MainController {
	@Autowired
	RegisterService registerService;

    @GetMapping("/")
    public String home() {
        return "login";
    }

    @GetMapping("/register2")
    public String registerPage() {
        return "register"; 
    }

    @GetMapping("/login2")
    public String loginPage() {
        return "login"; 
    }
    
    @GetMapping("/home2")
    public String HomePage() {
        return "home"; 
    }
    
    
    @GetMapping("/forgot-password")
    public String ForgetPage() {
        return "Forget.html"; 
    }
    
    @GetMapping("/Sending")
   public String SendingPage() {
	return "send-email";
}

    
    @GetMapping("/EmailTemplate")
    public String emailTemplate() {
 	return "EmailTemplate.html";
 }

}
