package com.example.algaworks.user.api;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.algaworks.user.domain.UserEntity;
import com.example.algaworks.user.domain.UserPasswordService;
import com.example.algaworks.user.domain.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PasswordResetController {

	private final UserPasswordService userPasswordService;
	private final UserRepository userRepository;
	
	
	@PostMapping("/public/forgot-password")
	public void forgotPassword(@RequestBody @Valid PasswordResetInput input) {
		System.out.println("chegando");
		Optional<UserEntity> optionalUser = userRepository.findByEmail(input.getEmail());
		optionalUser.ifPresent(user -> {
			String token = userPasswordService.generateToken(user);
			log.info("Enviando token por email...");
			System.out.println(token);
		});
	}
	
	@PostMapping("/public/change-password")
	public void changePassword(@RequestBody @Valid PasswordUpdateWithTokenInput input) {
		userPasswordService.changePassword(input.getPassword(), input.getToken());
	}
	
}


