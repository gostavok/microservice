package com.algaworks.example.mensagem.security;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SegurancaService {

	public String getUsuario() {
		var context = SecurityContextHolder.getContext();
		return context.getAuthentication().getName();
	}

	public boolean estaAutenticado() {
		if(getUsuario().equalsIgnoreCase("anonymousUser")) {
			return false;
		}
		SecurityContext context = SecurityContextHolder.getContext();
		return context.getAuthentication().isAuthenticated();
	}
	
	public boolean naoEstaAutenticado() {
		return !estaAutenticado();
	}
	
}