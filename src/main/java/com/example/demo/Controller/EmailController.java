package com.example.demo.Controller;


import java.util.List;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.Dto.EmailDTO;
import com.example.demo.Service.EmailService;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/emails")
public class EmailController {

    private final EmailService emailService;

   
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveEmailId(@RequestBody EmailDTO emailDTO) {
        
        String result = emailService.saveEmailId(emailDTO);
        if (result.equals("Data Saved Successfully")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(500).body(result);
        }
    }



    @GetMapping("/all")
    public ResponseEntity<List<EmailDTO>> getAllEmailId() {
        List<EmailDTO> emailList = emailService.getAllEmailId();
        if (emailList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(emailList, HttpStatus.OK);
    }
    @GetMapping("/resumeSend")
    public void mailSender() {
        emailService.sendEmailWithAttachment();
    }

    @GetMapping("/resumeSendToAll")
    public void mailSenderToAll() {
        emailService.sendEmailToAll();
     }
}
