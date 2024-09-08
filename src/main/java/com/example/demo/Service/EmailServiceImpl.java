package com.example.demo.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.example.demo.Dto.EmailDTO;
import com.example.demo.Entity.EmailEntity;
import com.example.demo.Entity.EmailLogEntity;
import com.example.demo.Repository.EmailLogRepository;
import com.example.demo.Repository.EmailRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {

	@Autowired
	private EmailRepository emailRepo;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private EmailLogRepository emailLogRepository;

	@Override
	public String saveEmailId(EmailDTO emailDTO) {
	    if (emailDTO == null || emailDTO.getEmailId() == null || emailDTO.getEmailId().trim().isEmpty()) {
	        return "Invalid email data provided!";
	    }

	    try {
	        
	        if (emailRepo.existsByEmailId(emailDTO.getEmailId())) {
	            return "Email already exists!";
	        }

	       
	        EmailEntity email = new EmailEntity();
	        email.setEmailId(emailDTO.getEmailId());
	        email.setCompanyName(emailDTO.getCompanyName());
	        email.setJobProfile(emailDTO.getJobProfile());
	        emailRepo.save(email);
	        return "Data Saved Successfully";
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "Error occurred while saving data";
	    }
	}


	@Override
	public List<EmailDTO> getAllEmailId() {
		try {
			List<EmailEntity> emailList = emailRepo.findAll();
			List<EmailDTO> emailDTOList = new ArrayList<>();
			for (EmailEntity emailEntity : emailList) {
				EmailDTO emailDTO = new EmailDTO();
				emailDTO.setEmailId(emailEntity.getEmailId());
				emailDTO.setId(emailEntity.getId());
				emailDTOList.add(emailDTO);
			}
			return emailDTOList;
		} catch (Exception e) {
			e.printStackTrace();
			return List.of();
		}
	}

	@Override
	public void sendEmailWithAttachment() {
		try {
			String toEmail = "pradeepkumarr7310@gmail.com";
			String subject = "Application for Java Developer";

			// Read HTML file
			Path htmlPath = Paths.get("src/main/resources/templates/EmailTemplate.html");
			String body = Files.readString(htmlPath, StandardCharsets.UTF_8);

			String attachmentPath = "src/main/Resume/Pradeep_ResumE.pdf";

			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			helper.setTo(toEmail);
			helper.setSubject(subject);
			helper.setText(body, true);

			FileSystemResource file = new FileSystemResource(new File(attachmentPath));
			helper.addAttachment("Resume.pdf", file);

			mailSender.send(message);
			System.out.println("Email sent successfully to " + toEmail);

		} catch (MessagingException | IOException e) {
			e.printStackTrace();
		}
	}

	public void sendEmailToAll() {
		List<EmailEntity> emailEntities = emailRepo.findAll();
		List<EmailLogEntity> emailLogEntities = emailLogRepository.findAll();
		Set<String> sentEmailAddresses = new HashSet<>();

		// Identify already sent email
		for (EmailLogEntity emailLogEntity : emailLogEntities) {
			if ("SENT".equals(emailLogEntity.getStatus())) {
				sentEmailAddresses.add(emailLogEntity.getRecipient());
			} else {
				emailLogEntity.setStatus("SENT");
				emailLogRepository.save(emailLogEntity); 
			}
		}

		// Send email if not already sent or if 48 hours have passed
		for (EmailEntity emailEntity : emailEntities) {
			String toEmail = emailEntity.getEmailId();
			List<EmailLogEntity> emailLogs = emailLogRepository.findByRecipient(toEmail);

			boolean shouldSendEmail = true;
			for (EmailLogEntity emailLog : emailLogs) {
				if ("SENT".equals(emailLog.getStatus())) {
					LocalDateTime lastSentTime = emailLog.getSentAt();
					if (lastSentTime != null) {
						Duration duration = Duration.between(lastSentTime, LocalDateTime.now());
						if (duration.toHours() < 48) {
							shouldSendEmail = false;
							System.out
									.println("Email already sent to " + toEmail + " less than 48 hours ago, skipping.");
							break; 
						}
					}
				}
			}

			if (!shouldSendEmail) {
				continue;
			}

			String subject = "Application for " + emailEntity.getJobProfile();
			String body = "";
			try {
				Path htmlPath = Paths.get("src/main/resources/templates/EmailTemplate.html");
				body = Files.readString(htmlPath, StandardCharsets.UTF_8);
				MimeMessage message = mailSender.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(message, true);
				helper.setTo(toEmail);
				helper.setSubject(subject);
				helper.setText(body, true);

				// Attach file
				String attachmentPath = "src/main/Resume/Pradeep_ResumE.pdf";
				FileSystemResource file = new FileSystemResource(new File(attachmentPath));
				helper.addAttachment("Resume.pdf", file);

				mailSender.send(message);

				// Log email after successful send
				logEmail(toEmail, subject, body, "SENT");
				System.out.println("Email sent and logged successfully to " + toEmail);

			} catch (IOException | MessagingException e) {
				// Log email after failure
				logEmail(toEmail, subject, body, "FAILED");
				System.err.println("Failed to send email to " + toEmail);
				e.printStackTrace();
			}
		}
	}

	private void logEmail(String toEmail, String subject, String body, String status) {
		EmailLogEntity emailLogEntity = new EmailLogEntity();
		emailLogEntity.setRecipient(toEmail);
		emailLogEntity.setSubject(subject);
		emailLogEntity.setBody(body);
		emailLogEntity.setStatus(status);
		emailLogEntity.setSentAt(LocalDateTime.now()); 
		emailLogRepository.save(emailLogEntity);
	}

}
