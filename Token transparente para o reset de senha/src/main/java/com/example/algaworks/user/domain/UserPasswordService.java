package com.example.algaworks.user.domain;

import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

import org.springframework.security.core.token.KeyBasedPersistenceTokenService;
import org.springframework.security.core.token.SecureRandomFactoryBean;
import org.springframework.security.core.token.Token;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Service
@RequiredArgsConstructor
public class UserPasswordService {

	private final UserRepository userRepo;
	private final PasswordEncoder passwordEncoder;
	
	@SneakyThrows
	public String generateToken(UserEntity user) {
		KeyBasedPersistenceTokenService tokenService = getInstanceFor(user);
		
		Token token = tokenService.allocateToken(user.getEmail());
		return token.getKey();
	}


	@SneakyThrows
	public void changePassword(String newPassword, String rawToken) {
		PasswordTokenPublicData publicData = readPublicData(rawToken);
		
		if(isExpired(publicData)) {
			throw new RuntimeException("Token expirado");
		}
				
		UserEntity userEntity = userRepo.findByEmail(publicData.getEmail()).orElseThrow(UserEntityNotFoundException::new);
		
		KeyBasedPersistenceTokenService tokenService = this.getInstanceFor(userEntity);
		tokenService.verifyToken(rawToken);
		
		userEntity.setPassword(this.passwordEncoder.encode(newPassword));
		userRepo.save(userEntity);
	}
	
	private boolean isExpired(PasswordTokenPublicData publicData) {
		Instant createdAt = new Date(publicData.getCreateAtTimestamp()).toInstant();
		Instant now = new Date().toInstant();
		return createdAt.plus(Duration.ofMinutes(5)).isBefore(now);
	}


	private KeyBasedPersistenceTokenService getInstanceFor(UserEntity user) throws Exception {
		KeyBasedPersistenceTokenService tokenService = new KeyBasedPersistenceTokenService();
		
		tokenService.setServerSecret(user.getPassword());
		tokenService.setServerInteger(16);
		tokenService.setSecureRandom(new SecureRandomFactoryBean().getObject());
		return tokenService;
	}
	

	private PasswordTokenPublicData readPublicData(String rawToken) {
		byte[] bytes = Base64.getDecoder().decode(rawToken);
		String rawTokenDecoded = new String(bytes);
		
		String[] tokenParts = rawTokenDecoded.split(":");
		
		Long timestamp = Long.parseLong(tokenParts[0]);
		String email = tokenParts[2];
		
		return new PasswordTokenPublicData(email, timestamp);
	}
	
}
