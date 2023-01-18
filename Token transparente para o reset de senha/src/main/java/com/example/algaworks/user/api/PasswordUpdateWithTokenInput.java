package com.example.algaworks.user.api;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class PasswordUpdateWithTokenInput {

	@NotBlank
	private String password;
	
	@NotBlank
	private String token;
	
}
