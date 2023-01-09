package com.ms.email.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.ms.email.enums.StatusEmail;
import com.ms.email.models.EmailModel;
import com.ms.email.repositories.EmailRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailService {

	@Autowired
	private EmailRepository emailRepository;
	
	@Autowired
	private JavaMailSender emailSender;

	public void sendEmail(EmailModel emailModel) {
		try {
			emailModel.setSendDataEmail(LocalDateTime.now());
			
			emailSender.send(generatedMessage(emailModel));			
			emailModel.setStatusEmail(StatusEmail.SENT);
			log.info("E-mail enviado com sucesso");
		} catch (MailException e) {
			log.error("Erro ao enviar e-mail {}", e.getMessage());
			emailModel.setStatusEmail(StatusEmail.ERROR);
		} finally {
			emailRepository.save(emailModel);			
		}
	}

	private SimpleMailMessage generatedMessage(EmailModel emailModel) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(emailModel.getEmailFrom());
		message.setTo(emailModel.getEmailTo());
		message.setSubject(emailModel.getSubject());
		message.setText(emailModel.getText());
		return message;
	}
	
}
