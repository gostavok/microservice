package com.example.algaworks.user.api;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class PasswordResetInput {

	@Email
	@NotBlank
	private String email;
	
}
