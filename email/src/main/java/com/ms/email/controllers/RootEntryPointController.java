package com.ms.email.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.ms.email.models.RootEntryPointModel;

@RestController
public class RootEntryPointController {

	@GetMapping("/")
	public RootEntryPointModel root() {
		RootEntryPointModel model = new RootEntryPointModel();
		model.add(linkTo(methodOn(EmailController.class).sendEmail(null)).withRel("Enviar email"));
		model.add(linkTo(methodOn(EmailController.class).consultarEmails(null,null)).withRel("Listar emails"));
		model.add(linkTo(methodOn(EmailController.class).consultarEmail(null)).withRel("Listar detalhes"));
		return model;
	}
	
}
