package com.ms.email.consumers;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.ms.email.dtos.EmailDTO;
import com.ms.email.models.EmailModel;
import com.ms.email.services.EmailService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class EmailConsumer {

	@Autowired
	private EmailService emailService;
	
	
	@RabbitListener(queues = "${spring.rabbitmq.queue}")
	public void listen(@Payload EmailDTO emailDTO) {
		EmailModel emailModel = new EmailModel();
		BeanUtils.copyProperties(emailDTO, emailModel);
		emailService.sendEmail(emailModel);
		
		log.info("Email status: {}", emailModel.getStatusEmail().toString());
	}
	
	
}
