package com.ms.email.controllers;


import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.ms.email.dtos.EmailDTO;
import com.ms.email.models.EmailModel;
import com.ms.email.services.EmailService;

import jakarta.validation.Valid;

@RestController
public class EmailController {

	@Autowired
	private EmailService emailService;
	
	
	@PostMapping("/sending-email")
	public ResponseEntity<EmailModel> sendEmail(@RequestBody @Valid EmailDTO emailDTO) {
		EmailModel emailModel = new EmailModel();
		BeanUtils.copyProperties(emailDTO, emailModel);
		emailService.sendEmail(emailModel);
		emailModel.add(linkTo(methodOn(EmailController.class).consultarEmails(null,null)).withRel("Listar emails"));
		return new ResponseEntity<>(emailModel, HttpStatus.CREATED);
	}
	
	
	@GetMapping("/consultar-email")
	public ResponseEntity<Page<EmailModel>> consultarEmails(@RequestParam(defaultValue = "20") Integer size,
														  @RequestParam(defaultValue = "0") Integer page){
		Page<EmailModel> emailPaginado = emailService.consultarEmails(PageRequest.of(page, size));
		
		emailPaginado.getContent().forEach(email -> 
								email.add(linkTo(methodOn(EmailController.class)
								.consultarEmail(email.getEmailId()))
								.withRel("Listar detalhes")));
		return new ResponseEntity<Page<EmailModel>>(emailPaginado, HttpStatus.OK);
	}
	
	@GetMapping("/consultar-email/{id}")
	public ResponseEntity<EmailModel> consultarEmail(@PathVariable(name = "id") UUID id){
		Optional<EmailModel> optional = emailService.consultarEmailPorId(id);
		
		if(optional.isPresent()) return new ResponseEntity<EmailModel>(optional.get(), HttpStatus.OK);
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
}
