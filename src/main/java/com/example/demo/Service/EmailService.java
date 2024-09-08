package com.example.demo.Service;

import java.util.List;

import com.example.demo.Dto.EmailDTO;

public interface EmailService {
    String saveEmailId(EmailDTO emailDTO);
    List<EmailDTO> getAllEmailId();
	public void sendEmailWithAttachment();
	public void sendEmailToAll();
	
}
